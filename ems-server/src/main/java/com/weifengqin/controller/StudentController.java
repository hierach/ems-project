package com.weifengqin.controller;

import com.weifengqin.dto.Result;
import com.weifengqin.dto.StudentSelectCourseDto;
import com.weifengqin.dto.UserChangePasswordDto;
import com.weifengqin.entity.Cst;
import com.weifengqin.service.StudentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author qin start
 * @create 2023-12-11-22:39
 */
@RequestMapping("/student")
@RestController
@CrossOrigin()
public class StudentController {

    @Resource
    private StudentService studentService;

    @GetMapping("/info")
    public Result info(String userId) {
        return studentService.getInfo(userId);
    }

    /**
     * 修改用户密码
     *
     * @param userChangePasswordDto
     * @return
     */
    @PutMapping("/changePassword")
    public Result changePassword(@RequestBody UserChangePasswordDto userChangePasswordDto) {
        return studentService.changePassword(userChangePasswordDto);
    }

    @GetMapping("/getCourseList")
    public Result getCourseList(String userId,@RequestParam(required = false) String courseName) {
        return studentService.getCourseList(userId,courseName);
    }

    /**
     * 选课方法
     *
     * @param selectCourseDto
     * @return
     */
    @PostMapping("/selectCourse")
    public Result selectCourse(@RequestBody StudentSelectCourseDto selectCourseDto) {
        return studentService.selectCourse(selectCourseDto);
    }

    /**
     * 查询得到已有课程
     * @param userId
     * @return
     */
    @GetMapping("/getHasSelectCourseList")
    public Result getHasSelectCourseList(String userId,@RequestParam(required = false) String courseName) {
        return studentService.getHasSelectCourseList(userId,courseName);
    }

    /**
     * 删除选择课程
     * @param userId
     * @param courseNum
     * @param techNum
     * @return
     */
    @DeleteMapping("/delCourse")
    public Result delCourse(String userId, String courseNum, String techNum) {

        return studentService.delCourse(userId,courseNum,techNum);
    }



    //学生请假申请
    @GetMapping("/getLeaveList")
    public Result getLeaveList(String stuNum){
        return studentService.getLeaveList(stuNum);
    }



    @PostMapping("/applyLeave")
    public Result applyLeave(@RequestBody Cst cst){
        return studentService.applyLeave(cst);
    }

    @DeleteMapping("/removeLeave")
    public Result removeLeave(String courseNum,String techNum,String stuNum){
        return studentService.removeLeave(courseNum,techNum,stuNum);
    }

    //展示学分信息
    @GetMapping("/getHasScores")
    public Result getHasScores(String stuNum){
        return studentService.getHasScores(stuNum);
    }

    //获取学生课程表
    @GetMapping("/getTimeTableList")
    public Result getTimeTableList(String stuNum){
        return studentService.getTimeTableList(stuNum);
    }
 }
