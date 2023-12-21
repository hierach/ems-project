package com.weifengqin.controller;

import com.weifengqin.dto.Result;
import com.weifengqin.dto.UserChangePasswordDto;
import com.weifengqin.entity.Cst;
import com.weifengqin.entity.Open;
import com.weifengqin.service.TeacherService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author qin start
 * @create 2023-12-15-15:42
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private TeacherService teacherService;


    @GetMapping("/info")
    public Result getInfo(String userId){
        return teacherService.getInfo(userId);
    }

    @PutMapping("/changePassword")
    public Result getInfo(@RequestBody UserChangePasswordDto changePasswordDto){
        return teacherService.changePassword(changePasswordDto);
    }

    @GetMapping("/getOpenList")
    public Result getOpenList(String userId,@RequestParam(required = false) String courseName){
        return teacherService.getOpenList(userId,courseName);
    }

    @PostMapping("/openCourse")
    public Result openCourse(@RequestBody Open open){
        return teacherService.openCourse(open);
    }

    @PutMapping("/editCourse")
    public Result editCourse(@RequestBody Open open){
        return teacherService.editCourse(open);
    }

    @DeleteMapping("/deleteCourse")
    public Result deleteCourse( String techNum, String courseNum, String adminNum){
        return teacherService.deleteCourse(techNum,courseNum,adminNum);
    }

    //打分管理
    @GetMapping("/getMarkList")
    public Result getMarkList(String techNum,@RequestParam(required = false) String courseName){
        return teacherService.getMarkList(techNum,courseName);
    }


    @PutMapping("/markOperate")
    public Result markOperate(@RequestBody Cst cst){
        return teacherService.markOperate(cst);
    }


    //管理请假请求

    @GetMapping("/getLeaveList")
    public Result getLeaveList(String techNum){
        return teacherService.getLeaveList(techNum);
    }

    @PostMapping("/allowApplyLeave")
    public Result allowApplyLeave(@RequestBody Cst cst){
        return teacherService.allowApplyLeave(cst);
    }

    @DeleteMapping("/rejectApplyLeave")
    public Result rejectApplyLeave(String courseNum, String techNum, String stuNum){
        return teacherService.rejectApplyLeave(courseNum,techNum,stuNum);
    }

}
