package com.weifengqin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weifengqin.dto.Result;
import com.weifengqin.dto.StudentSelectCourseDto;
import com.weifengqin.dto.UserChangePasswordDto;
import com.weifengqin.dto.UserDto;
import com.weifengqin.entity.*;
import com.weifengqin.mapper.CourseMapper;
import com.weifengqin.service.*;
import com.weifengqin.mapper.StudentMapper;
import com.weifengqin.utils.RedisConstants;
import com.weifengqin.utils.SystemConstants;
import com.weifengqin.vo.StuLeaveVo;
import com.weifengqin.vo.LoginVo;
import com.weifengqin.vo.StudentCourseVo;
import com.weifengqin.vo.TimeTableVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author 666
* @description 针对表【student】的数据库操作Service实现
* @createDate 2023-12-11 14:52:10
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CstService cstService;

    @Resource
    private CourseService courseService;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private StudentService studentService;

    @Resource
    private OpenService openService;

    @Resource
    private TeacherService teacherService;

    @Resource
    private StudentMapper studentMapper;


    /**
     * 学生登录
     * @param userDto 学生登录数据
     * @return
     */
    @Override
    public Result login(UserDto userDto) {

        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getStuNum,userDto.getUserNum());
        queryWrapper.eq(Student::getPassword,userDto.getPassword());

        Student student = getOne(queryWrapper);
        //账号或者密码错误
        if(Objects.isNull(student)){
            return Result.fail(401,"账号或密码有误，请重新登陆");
        }
        //进行内容拷贝
        userDto.setName(student.getName());

        //把密码设置为空
        userDto.setPassword("");


        //下面是登陆成功内容
        //生成token
        String token = UUID.randomUUID().toString(true);


        // 把Map中的Key和Value都改成了String,便于之后转换
        Map<String, Object> userMap = BeanUtil.beanToMap(userDto, new HashMap<>()
                , CopyOptions.create()
                        .ignoreNullValue()
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));

        // 4.3 存入redis，并设置有效期
        stringRedisTemplate.opsForHash().putAll(RedisConstants.LOGIN_USER_KEY + token,userMap);

        //设置有效期30min
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token,RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        String userId = userDto.getUserNum();
        String userName = userDto.getName();
        LoginVo loginVo = new LoginVo(token,userId,userName);
        //4.4 返回token
        return Result.ok(loginVo);

    }

    /**
     * 获得学生信息
     * @return
     */
    @Override
    public Result getInfo(String userId) {


        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getStuNum,userId);

        Student student = getOne(queryWrapper);

//        System.out.println("getInfo:" + student);

        return Result.ok(student);
    }

    /**
     * 修改用户密码
     * @param userChangePasswordDto
     * @return
     */
    @Override
    public Result changePassword(UserChangePasswordDto userChangePasswordDto) {

        LambdaUpdateWrapper<Student> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Student::getStuNum,userChangePasswordDto.getUserId());
        updateWrapper.set(Student::getPassword,userChangePasswordDto.getPassword());

        update(updateWrapper);
        return Result.ok();
    }


    /**
     * 根据学生id得出可以选择的课程列表
     *
     * @param courseName 可选值
     * @param userId
     * @return
     */
    @Override
    public Result getCourseList(String userId, String courseName) {

        //根据userId查询出已选课程号
        String[] validCourseNum = cstService.selectValidCourseNum(userId);
        //排除已选课程号，返回选课列表
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(courseName),Course::getCourseName,courseName);
        queryWrapper.notIn(!Objects.isNull(validCourseNum) && validCourseNum.length != 0,Course::getCourseNum,validCourseNum);

        List<Course> courseList = courseService.list(queryWrapper);

        //查询这些课程的开课教师
        List<StudentCourseVo> studentCourseVoList = courseList.stream().map(course -> {

            //创建展示vo
            StudentCourseVo studentCourseVo = new StudentCourseVo();
            BeanUtil.copyProperties(course, studentCourseVo);

            //根据课程id，去open表中查询教师id，然后教师id去教师表中查询教师名字
            LambdaQueryWrapper<Open> openLambdaQueryWrapper = new LambdaQueryWrapper<>();
            openLambdaQueryWrapper.eq(Open::getCourseNum, course.getCourseNum());
            Open open = openService.getOne(openLambdaQueryWrapper);
            String techNum = open.getTechNum();
            studentCourseVo.setTechNum(techNum);

            //根据techNum去教师表查询教师名字
            LambdaQueryWrapper<Teacher> teacherLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teacherLambdaQueryWrapper.eq(Teacher::getTechNum, techNum);
            Teacher teacher = teacherService.getOne(teacherLambdaQueryWrapper);
            studentCourseVo.setTechName(teacher.getName());

            return studentCourseVo;
        }).collect(Collectors.toList());


        return Result.ok(studentCourseVoList);
    }


    /**
     * 选课逻辑
     * @param selectCourseDto
     * @return
     */
    @Override
    @Transactional
    public Result selectCourse(StudentSelectCourseDto selectCourseDto) {

        String userId = selectCourseDto.getUserId();
        String courseNum = selectCourseDto.getCourseNum();
        String techNum = selectCourseDto.getTechNum();

        if(Objects.isNull(userId) || Objects.isNull(courseNum) || Objects.isNull(techNum)){
            return Result.fail("选课数据传递非法!");
        }

        //1.进行合法性校验
        LambdaQueryWrapper<Course> courseQueryMapper = new LambdaQueryWrapper<>();
        courseQueryMapper.eq(Course::getCourseNum,courseNum);
        Course course = courseService.getOne(courseQueryMapper);

        LambdaQueryWrapper<Student> studentQueryMapper = new LambdaQueryWrapper<>();
        studentQueryMapper.eq(Student::getStuNum,userId);
        Student student = studentService.getOne(studentQueryMapper);

        //1.1 选课时间校验
        LocalDateTime startTime = course.getStartTime();
        LocalDateTime endTime = course.getEndTime();
        LocalDateTime now = LocalDateTime.now();
        boolean isNowBetweenStartAndEnd = now.isAfter(startTime) && now.isBefore(endTime);

        if (!isNowBetweenStartAndEnd){
            return Result.fail("当前选课时间不在课程范围内，选课失败!");
        }

        //1.2 检查选课时间是否冲突
        Integer selWeek = course.getSelWeek();
        Integer start = course.getSelStart();
        Integer end = course.getSelEnd();
        List<Course> hasSelCourseList = courseMapper.selectCourseByStudentId(userId);
        boolean isConflict = false;//标记变量是否冲突
        for(Course c : hasSelCourseList){

            if(c.getSelWeek().equals(selWeek)){
                // 前提：start 和 end 表示闭合的开区间
                // 如果c.getSelStart(), c.getSelEnd()和已有的start和end重叠，把isConflict变量设置为true
                if ((start >= c.getSelStart() && start <= c.getSelEnd()) || (end >= c.getSelStart() && end <= c.getSelEnd()) || (c.getSelStart() >= start && c.getSelStart() <= end) || (c.getSelEnd() >= start && c.getSelEnd() <= end)) {
                    isConflict = true;
                    break; // 如果发现冲突，可以提前结束循环
                }
            }
        }

        if(isConflict){
            return Result.fail("选课时间与已选课程有冲突，选课失败!");
        }

        //1.3 检查学分是否达到了最大学分
        int sumScore = hasSelCourseList.stream().mapToInt(Course::getScore).sum();
        if((sumScore + course.getScore()) > student.getNeedScore()){
            return Result.fail("超过最大选课学分上限，选课失败!");
        }


        //1.4 检查选课人数是否超过了最大人数 加锁保护 Course属于共享资源
        synchronized (StudentServiceImpl.class){
            if((course.getSelPeople() + 1) > course.getMaxPeople()){
                return Result.fail("超过最大课程人数上限，选课失败!");
            }
        }

        //2. 校验通过 修改课程表中的选课人数并且插入选课表中
        // 更新两张表，要么同时成功，要么同时失败
        LambdaUpdateWrapper<Course> courseUpdateWrapper = new LambdaUpdateWrapper<>();
        courseUpdateWrapper.set(Course::getSelPeople,course.getSelPeople() + 1);
        courseUpdateWrapper.eq(Course::getCourseNum,courseNum);
        courseService.update(courseUpdateWrapper);

        Cst cst = new Cst(courseNum,userId,techNum);
        cstService.save(cst);
        return Result.ok("选课成功!");
    }


    /**
     * 得到已经选到的课的列表 参考getCourseList
     * @param userId
     * @return
     */
    @Override
    public Result getHasSelectCourseList(String userId,String courseName) {
        //根据userId查询出已选课程号
        String[] validCourseNum = cstService.selectValidCourseNum(userId);
        if(Objects.isNull(validCourseNum) || validCourseNum.length == 0){
            //查询数据为空，直接返回
           return Result.ok();
        }
        //选择已选择课程号，返回课表
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(courseName),Course::getCourseName,courseName);

        queryWrapper.in(Course::getCourseNum,validCourseNum);

        List<Course> courseList = courseService.list(queryWrapper);

        //查询这些课程的开课教师
        List<StudentCourseVo> studentCourseVoList = courseList.stream().map(course -> {

            //创建展示vo
            StudentCourseVo studentCourseVo = new StudentCourseVo();
            BeanUtil.copyProperties(course, studentCourseVo);

            //根据课程id，去open表中查询教师id，然后教师id去教师表中查询教师名字
            LambdaQueryWrapper<Open> openLambdaQueryWrapper = new LambdaQueryWrapper<>();
            openLambdaQueryWrapper.eq(Open::getCourseNum, course.getCourseNum());
            Open open = openService.getOne(openLambdaQueryWrapper);
            String techNum = open.getTechNum();
            studentCourseVo.setTechNum(techNum);

            //根据techNum去教师表查询教师名字
            LambdaQueryWrapper<Teacher> teacherLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teacherLambdaQueryWrapper.eq(Teacher::getTechNum, techNum);
            Teacher teacher = teacherService.getOne(teacherLambdaQueryWrapper);
            studentCourseVo.setTechName(teacher.getName());

            //根据stuNum，courseNum和techNum去cst表中查询成绩
            LambdaQueryWrapper<Cst> markLambdaQueryWrapper = new LambdaQueryWrapper<>();
            markLambdaQueryWrapper.eq(Cst::getStuNum,userId);
            markLambdaQueryWrapper.eq(Cst::getCourseNum,studentCourseVo.getCourseNum());
            markLambdaQueryWrapper.eq(Cst::getTechNum,studentCourseVo.getTechNum());
            Cst cst = cstService.getOne(markLambdaQueryWrapper);
            if(!Objects.isNull(cst)){
                studentCourseVo.setMark(cst.getMark());
            }


            return studentCourseVo;
        }).collect(Collectors.toList());

        studentCourseVoList.sort(new Comparator<StudentCourseVo>() {
            /**
             * 把已经有得分的课排在最前面
             * @param o1
             * @param o2
             * @return
             */
            @Override
            public int compare(StudentCourseVo o1, StudentCourseVo o2) {
                if(o1.getMark().equals(SystemConstants.MARK_INVALID) && o2.getMark().equals(SystemConstants.MARK_INVALID)){
                    return 0;
                }else if(o1.getMark().equals(SystemConstants.MARK_INVALID)){
                    return 1;
                }else{
                    return -1;
                }
            }
        });


        return Result.ok(studentCourseVoList);
    }


    /**
     * 删除选择课程
     *
     * @param userId
     * @param courseNum
     * @param techNum
     * @return
     */
    @Override
    @Transactional
    public Result delCourse(String userId, String courseNum, String techNum) {

        //1. 删除cst表中课程
        LambdaQueryWrapper<Cst> cstLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cstLambdaQueryWrapper.eq(Cst::getCourseNum,courseNum);
        cstLambdaQueryWrapper.eq(Cst::getStuNum,userId);
        cstLambdaQueryWrapper.eq(Cst::getTechNum,techNum);
        cstService.remove(cstLambdaQueryWrapper);

        //2.让course选课次数减1
        LambdaUpdateWrapper<Course> courseLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        courseLambdaUpdateWrapper.eq(Course::getCourseNum, courseNum) // 添加更新条件
                .setSql("sel_people = sel_people - 1");

        courseService.update(courseLambdaUpdateWrapper);


        return Result.ok();
    }


    @Override
    public Result getLeaveList(String stuNum) {

        List<StuLeaveVo> stuLeaveVoList = studentMapper.getLeaveList(stuNum);

        return Result.ok(stuLeaveVoList);
    }

    /**
     * 申请离开
     * @param cst
     * @return
     */
    @Override
    public Result applyLeave(Cst cst) {
        LambdaUpdateWrapper<Cst> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Cst::getCourseNum,cst.getCourseNum());
        updateWrapper.eq(Cst::getTechNum,cst.getTechNum());
        updateWrapper.eq(Cst::getStuNum,cst.getStuNum());

        updateWrapper.set(Cst::getLeaveStart,cst.getLeaveStart());
        updateWrapper.set(Cst::getLeaveEnd,cst.getLeaveEnd());
        updateWrapper.set(Cst::getLeaveStatus, SystemConstants.LEAVE_STATUS_APPLY);

        cstService.update(updateWrapper);

        return Result.ok();
    }

    /**
     * 删除申请。不是真正地删除一条记录，而是设置leaveStatus为0来达到 并且清空时间
     * @param courseNum
     * @param techNum
     * @param stuNum
     * @return
     */
    @Override
    public Result removeLeave(String courseNum, String techNum, String stuNum) {
        LambdaUpdateWrapper<Cst> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Cst::getCourseNum,courseNum);
        updateWrapper.eq(Cst::getTechNum,techNum);
        updateWrapper.eq(Cst::getStuNum,stuNum);

        updateWrapper.set(Cst::getLeaveStart,null);
        updateWrapper.set(Cst::getLeaveEnd,null);
        updateWrapper.set(Cst::getLeaveStatus, SystemConstants.LEAVE_STATUS_NOT_APPLY);

        cstService.update(updateWrapper);

        return Result.ok();
    }

    /**
     * 获得已经获取的学分
     * @param stuNum
     * @return
     */
    @Override
    public Result getHasScores(String stuNum) {
        List<Integer> scoreList = studentMapper.getHasScores(stuNum);

        int sum = scoreList.stream().mapToInt(score -> score).sum();


        //返回求和结果
        return Result.ok(sum);
    }

    /**
     * 获取学生已选课程 且 未打分课程的课程表。打分了算结课，不应该在课程表中
     * @param stuNum
     * @return
     */
    @Override
    public Result getTimeTableList(String stuNum) {
        List<TimeTableVo> timeTableVoList = studentMapper.getTimeTableList(stuNum);

        //对timeTableVoList的context进行赋值处理
        List<TimeTableVo> timeTableVoList1 = timeTableVoList.stream().map(timeTableVo -> {
            String formatStartTime = timeTableVo.getStartTime().format(DateTimeFormatter.ofPattern("yy.MM.dd"));
            String formatEndTime = timeTableVo.getEndTime().format(DateTimeFormatter.ofPattern("yy.MM.dd"));
            String place = timeTableVo.getPlace();

            String content = formatStartTime + "-</br>" + formatEndTime + "</br>" + place;
            timeTableVo.setContent(content);

            return timeTableVo;
        }).collect(Collectors.toList());


        return Result.ok(timeTableVoList1);
    }
}




