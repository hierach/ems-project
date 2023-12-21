package com.weifengqin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weifengqin.dto.Result;
import com.weifengqin.dto.UserChangePasswordDto;
import com.weifengqin.dto.UserDto;
import com.weifengqin.entity.*;
import com.weifengqin.mapper.CstMapper;
import com.weifengqin.service.*;
import com.weifengqin.mapper.AdminMapper;
import com.weifengqin.utils.RedisConstants;
import com.weifengqin.utils.SystemConstants;
import com.weifengqin.vo.LoginVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
* @author 666
* @description 针对表【admin】的数据库操作Service实现
* @createDate 2023-12-11 16:46:17
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private StudentService studentService;

    @Resource
    private TeacherService teacherService;

    @Resource
    private OpenService openService;

    @Resource
    private CourseService courseService;

    @Resource
    private CstService cstService;

    @Resource
    private CstMapper cstMapper;

    @Override
    public Result login(UserDto userDto) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Admin::getAdminNum,userDto.getUserNum());
        queryWrapper.eq(Admin::getPassword,userDto.getPassword());

        Admin admin = getOne(queryWrapper);

        //账号或者密码错误
        if(Objects.isNull(admin)){
            return Result.fail(401,"账号或密码有误，请重新登陆");
        }

        //进行内容拷贝
        userDto.setName(admin.getName());

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

    @Override
    public Result getList() {
        return Result.ok(list());
    }


    @Override
    public Result info(String userId) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getAdminNum,userId);

        Admin admin = getOne(queryWrapper);


        return Result.ok(admin);
    }

    @Override
    public Result changePassword(UserChangePasswordDto changePasswordDto) {
        LambdaUpdateWrapper<Admin> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Admin::getAdminNum,changePasswordDto.getUserId());
        updateWrapper.set(Admin::getPassword,changePasswordDto.getPassword());

        update(updateWrapper);
        return Result.ok();
    }

    /**
     * 获取学生列表
     * @param stuName
     * @return
     */
    @Override
    public Result getStudentList(String stuName) {

        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(stuName),Student::getName,stuName);

        List<Student> studentList = studentService.list(queryWrapper);


        return Result.ok(studentList);
    }

    @Override
    public Result addStudent(Student student) {

        studentService.save(student);

        return Result.ok();
    }

    @Override
    public Result editStudent(Student student) {
        LambdaUpdateWrapper<Student> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Student::getId, student.getId());
        studentService.update(student,updateWrapper);

        return Result.ok();
    }

    @Override
    @Transactional
    public Result deleteStudent(String stuNum) {
        //删除所有有关系的记录

        //删除学生表记录
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getStuNum,stuNum);
        studentService.remove(queryWrapper);

        //删除CST表记录
        LambdaUpdateWrapper<Cst> cstLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        cstLambdaUpdateWrapper.eq(Cst::getStuNum,stuNum);
        cstService.remove(cstLambdaUpdateWrapper);


        return Result.ok();
    }

    @Override
    public Result getTeacherList(String name) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Teacher::getName,name);

        List<Teacher> teacherList = teacherService.list(queryWrapper);


        return Result.ok(teacherList);
    }

    @Override
    public Result addTeacher(Teacher teacher) {
        teacherService.save(teacher);

        return Result.ok();
    }

    @Override
    public Result editTeacher(Teacher teacher) {
        LambdaUpdateWrapper<Teacher> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Teacher::getId, teacher.getId());
        teacherService.update(teacher,updateWrapper);

        return Result.ok();
    }

    @Override
    @Transactional
    public Result deleteTeacher(String techNum) {
        //删除所有与教师有关的记录，包括课程相关信息

        //删除教师表记录
        LambdaUpdateWrapper<Teacher> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Teacher::getTechNum, techNum);
        teacherService.remove(updateWrapper);

        //先查询出cst表的课程id
        List<String> courseNumList = cstMapper.getCourseNumListByTechNum(techNum);

        //删除cst记录
        LambdaQueryWrapper<Cst> cstLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cstLambdaQueryWrapper.eq(Cst::getTechNum,techNum);
        cstService.remove(cstLambdaQueryWrapper);
        //删除course记录
        LambdaUpdateWrapper<Course> courseLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        courseLambdaUpdateWrapper.in(Course::getCourseNum,courseNumList);
        courseService.remove(courseLambdaUpdateWrapper);
        //删除open记录
        LambdaQueryWrapper<Open> openLambdaQueryWrapper = new LambdaQueryWrapper<>();
        openLambdaQueryWrapper.eq(Open::getTechNum,techNum);
        openService.remove(openLambdaQueryWrapper);


        return Result.ok();
    }

    @Override
    public Result getWaitOpenList(String userId) {


        LambdaQueryWrapper<Open> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Open::getAdminNum,userId);
        queryWrapper.eq(Open::getStatus, SystemConstants.WAIT_STATUS);

        List<Open> openList = openService.list(queryWrapper);
        return Result.ok(openList);
    }

    /**
     * 开课请求
     * @param open
     * @return
     */
    @Override
    @Transactional
    public Result permitCourse(Open open) {

        //1.设置open表的字段status为1
        open.setStatus(SystemConstants.OPEN_STATUS);
        LambdaUpdateWrapper<Open> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Open::getAdminNum,open.getAdminNum());
        updateWrapper.eq(Open::getTechNum,open.getTechNum());
        updateWrapper.eq(Open::getCourseNum,open.getCourseNum());
        openService.update(open,updateWrapper);


        //2.更新进course表中
        Course course = BeanUtil.copyProperties(open, Course.class);
        courseService.save(course);

        return Result.ok();
    }


    @Override
    public Result deleteCourse(String adminNum,String courseNum,String techNum) {

        //设置open表的字段status为1
        LambdaUpdateWrapper<Open> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Open::getAdminNum,adminNum);
        updateWrapper.eq(Open::getTechNum,techNum);
        updateWrapper.eq(Open::getCourseNum,courseNum);
        updateWrapper.set(Open::getStatus,SystemConstants.DELETE_STATUS);
        openService.update(updateWrapper);


        return Result.ok();
    }
}




