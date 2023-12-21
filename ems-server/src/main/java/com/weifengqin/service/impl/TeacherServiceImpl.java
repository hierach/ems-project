package com.weifengqin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weifengqin.dto.Result;
import com.weifengqin.dto.UserChangePasswordDto;
import com.weifengqin.dto.UserDto;
import com.weifengqin.entity.*;
import com.weifengqin.mapper.OpenMapper;
import com.weifengqin.service.*;
import com.weifengqin.mapper.TeacherMapper;
import com.weifengqin.utils.RedisConstants;
import com.weifengqin.utils.SystemConstants;
import com.weifengqin.vo.LoginVo;
import com.weifengqin.vo.MarkVo;
import com.weifengqin.vo.TechLeaveVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.error.Mark;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author 666
* @description 针对表【teacher】的数据库操作Service实现
* @createDate 2023-12-11 16:46:07
*/
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
    implements TeacherService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private OpenService openService;

    @Resource
    private OpenMapper openMapper;

    @Resource
    private CourseService courseService;

    @Resource
    private CstService cstService;



    @Resource
    private TeacherMapper teacherMapper;




    @Override
    public Result login(UserDto userDto) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Teacher::getTechNum,userDto.getUserNum());
        queryWrapper.eq(Teacher::getPassword,userDto.getPassword());

        Teacher teacher = getOne(queryWrapper);

        //账号或者密码错误
        if(Objects.isNull(teacher)){
            return Result.fail(401,"账号或密码有误，请重新登陆");
        }

        //进行内容拷贝
        userDto.setName(teacher.getName());

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
    public Result getInfo(String userId) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getTechNum,userId);

        Teacher teacher = getOne(queryWrapper);


        return Result.ok(teacher);
    }

    @Override
    public Result changePassword(UserChangePasswordDto changePasswordDto) {
        LambdaUpdateWrapper<Teacher> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Teacher::getTechNum,changePasswordDto.getUserId());
        updateWrapper.set(Teacher::getPassword,changePasswordDto.getPassword());

        update(updateWrapper);
        return Result.ok();
    }

    /**
     * 得到开课列表
     * @param userId
     * @param courseName
     * @return
     */
    @Override
    public Result getOpenList(String userId,String courseName) {

        LambdaQueryWrapper<Open> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Open::getTechNum,userId);
        queryWrapper.like(StringUtils.hasText(courseName),Open::getCourseName,courseName);
        List<Open> openList = openService.list(queryWrapper);


        return Result.ok(openList);
    }


    /**
     * 开课
     * @param open
     * @return
     */
    @Override
    @Transactional
    public Result openCourse(Open open) {

        //1.合法性检查，主要检查开课时间是否与已选课程冲突
        Integer selWeek = open.getSelWeek();
        Integer start = open.getSelStart();
        Integer end = open.getSelEnd();
        LambdaQueryWrapper<Open> openLambdaQueryWrapper = new LambdaQueryWrapper<>();
        openLambdaQueryWrapper.eq(Open::getTechNum,open.getTechNum());
        List<Open> openList = openService.list(openLambdaQueryWrapper);
        boolean isConflict = false;//标记变量是否冲突
        for(Open c : openList){

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
            return Result.fail("开课时间与已开课程有冲突，开课失败!");
        }

        //2.准备添加
        // 额外处理的两个字段  status 和 courseNum
        // status默认是0，courseNum需要截取生成
        List<Open> allOpenList = openService.list();
        List<String> courseNumList = allOpenList.stream().map(Open::getCourseNum).collect(Collectors.toList());
        Collections.sort(courseNumList, (o1, o2) -> {
            // 从字符串中提取数字部分进行比较
            int num1 = Integer.parseInt(o1.substring(1));
            int num2 = Integer.parseInt(o2.substring(1));
            return Integer.compare(num1, num2);
        });


        String maxString = courseNumList.get(courseNumList.size() - 1);
        int maxNum = Integer.parseInt(maxString.substring(1));

        String nextNum = String.format("C%03d", maxNum + 1);

        //3.添加 不用往course表中添加，这是管理员批准的功能
        open.setStatus(0);
        open.setCourseNum(nextNum);
        openService.save(open);


        return Result.ok();
    }

    /**
     * 修改开课信息
     * @param open
     * @return
     */
    @Override
    public Result editCourse(Open open) {
        //主要还是检查上课冲突问题
        Integer selWeek = open.getSelWeek();
        Integer start = open.getSelStart();
        Integer end = open.getSelEnd();
        LambdaQueryWrapper<Open> openLambdaQueryWrapper = new LambdaQueryWrapper<>();
        openLambdaQueryWrapper.eq(Open::getTechNum,open.getTechNum());
        List<Open> openList = openService.list(openLambdaQueryWrapper);
        boolean isConflict = false;//标记变量是否冲突
        for(Open c : openList){
            if(c.getCourseNum().equals(open.getCourseNum())){
                //不检查自身的时间冲突
                continue;
            }
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
            return Result.fail("修改的开课时间与已开课程有冲突，修改失败!");
        }

        //2.检查过之后，直接更新相应的字段
        LambdaUpdateWrapper<Open> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Open::getCourseNum,open.getCourseNum());
        updateWrapper.eq(Open::getAdminNum,open.getAdminNum());
        updateWrapper.eq(Open::getTechNum,open.getTechNum());
        openService.update(open,updateWrapper);

        return Result.ok();
    }

    /**
     * 因为课程涉及多张表，所以凡是涉及课程的表都要参与进来
     * @param techNum
     * @param courseNum
     * @param adminNum
     * @return
     */
    @Transactional
    @Override
    public Result deleteCourse(String techNum, String courseNum, String adminNum) {

        if(!StringUtils.hasText(techNum) || !StringUtils.hasText(courseNum) || !StringUtils.hasText(adminNum)){
            return Result.fail("删除失败，提供删除信息不完整");
        }

        //1.open表
        LambdaQueryWrapper<Open> openLambdaQueryWrapper = new LambdaQueryWrapper<>();
        openLambdaQueryWrapper.eq(Open::getTechNum,techNum);
        openLambdaQueryWrapper.eq(Open::getCourseNum,courseNum);
        openLambdaQueryWrapper.eq(Open::getAdminNum,adminNum);

        openService.remove(openLambdaQueryWrapper);

        //2.course表  根据course_num号进行删除
        LambdaQueryWrapper<Course> courseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseLambdaQueryWrapper.eq(Course::getCourseNum,courseNum);
        courseService.remove(courseLambdaQueryWrapper);


        //3. cst表
        LambdaQueryWrapper<Cst> cstLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cstLambdaQueryWrapper.eq(Cst::getCourseNum,courseNum);
        cstService.remove(cstLambdaQueryWrapper);

        return Result.ok("删除成功");
    }


    /**
     * 得到打分列表  返回MarkVo
     * @param techNum
     * @param courseName
     * @return
     */
    @Override
    public Result getMarkList(String techNum, String courseName) {

        //连表查询
        List<MarkVo> markList = teacherMapper.getMarkList(techNum,courseName);

        return Result.ok(markList);
    }

    /**
     * 修改cst表打分成绩
     * @param cst
     * @return
     */
    @Override
    public Result markOperate(Cst cst) {

        LambdaUpdateWrapper<Cst> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Cst::getCourseNum,cst.getCourseNum());
        updateWrapper.eq(Cst::getStuNum,cst.getStuNum());
        updateWrapper.eq(Cst::getTechNum,cst.getTechNum());
        updateWrapper.set(Cst::getMark,cst.getMark());

        cstService.update(updateWrapper);

        return Result.ok();
    }


    @Override
    public Result getLeaveList(String techNum) {
        List<TechLeaveVo> techLeaveVoList = teacherMapper.getLeaveList(techNum);
        return Result.ok(techLeaveVoList);
    }

    @Override
    public Result allowApplyLeave(Cst cst) {
        LambdaUpdateWrapper<Cst> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Cst::getCourseNum,cst.getCourseNum());
        updateWrapper.eq(Cst::getTechNum,cst.getTechNum());
        updateWrapper.eq(Cst::getStuNum,cst.getStuNum());

        updateWrapper.set(Cst::getLeaveStart,cst.getLeaveStart());
        updateWrapper.set(Cst::getLeaveEnd,cst.getLeaveEnd());
        updateWrapper.set(Cst::getLeaveStatus, SystemConstants.ALLOW_STATUS_APPLY);
        cstService.update(updateWrapper);

        return Result.ok();
    }

    @Override
    public Result rejectApplyLeave(String courseNum, String techNum, String stuNum) {
        LambdaUpdateWrapper<Cst> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Cst::getCourseNum,courseNum);
        updateWrapper.eq(Cst::getTechNum,techNum);
        updateWrapper.eq(Cst::getStuNum,stuNum);

        updateWrapper.set(Cst::getLeaveStatus, SystemConstants.REJECT_STATUS_NOT_APPLY);
        cstService.update(updateWrapper);


        return Result.ok();
    }
}




