package com.weifengqin.controller;

import com.weifengqin.dto.Result;
import com.weifengqin.dto.UserChangePasswordDto;
import com.weifengqin.entity.Open;
import com.weifengqin.entity.Student;
import com.weifengqin.entity.Teacher;
import com.weifengqin.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author qin start
 * @create 2023-12-17-15:03
 */
@RestController
@RequestMapping("/admin")
public class AdminController {


    @Resource
    private AdminService adminService;

    @GetMapping("/getAdminList")
    public Result getList(){
        return adminService.getList();
    }


    @GetMapping("/info")
    public Result info(String userId){
        return adminService.info(userId);
    }


    @PutMapping("/changePassword")
    public Result changePassword(@RequestBody UserChangePasswordDto changePasswordDto){
        return adminService.changePassword(changePasswordDto);
    }


    // 学生管理

    @GetMapping("/getStudentList")
    public Result getStudentList(@RequestParam(required = false) String stuName){
        return adminService.getStudentList(stuName);
    }

    @PostMapping("/addStudent")
    public Result addStudent(@RequestBody Student student){
        return adminService.addStudent(student);
    }

    @PutMapping("/editStudent")
    public Result editStudent(@RequestBody Student student){
        return adminService.editStudent(student);
    }

    @DeleteMapping("/deleteStudent")
    public Result deleteStudent(String stuNum){
        return adminService.deleteStudent(stuNum);
    }


    // 教师管理
    @GetMapping("/getTeacherList")
    public Result getTeacherList(@RequestParam(required = false) String name){
        return adminService.getTeacherList(name);
    }

    @PostMapping("/addTeacher")
    public Result addTeacher(@RequestBody Teacher teacher){
        return adminService.addTeacher(teacher);
    }

    @PutMapping("/editTeacher")
    public Result editTeacher(@RequestBody Teacher teacher){
        return adminService.editTeacher(teacher);
    }

    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(String techNum){
        return adminService.deleteTeacher(techNum);
    }

    //开课请求相关方法

    /**
     * 获取根据当前的管理员id，获取他所管辖的status为0的课程
     * @param userId
     * @return
     */
    @GetMapping("/getWaitOpenList")
    public Result getWaitOpenList(String userId){
        return adminService.getWaitOpenList(userId);
    }

    /**
     * 批准开课请求，修改open表和在course表中增加记录
     * @param open
     * @return
     */
    @PostMapping("/permitCourse")
    public Result permitCourse(@RequestBody Open open){
        return adminService.permitCourse(open);
    }

    /**
     * 撤销开课请求，把open表状态设置为-1即可，至于删不删课程，由教师决定
     * @param adminNum
     * @param courseNum
     * @param techNum
     * @return
     */
    @DeleteMapping("/deleteCourse")
    public Result deleteCourse(String adminNum,String courseNum,String techNum){
        return adminService.deleteCourse(adminNum,courseNum,techNum);
    }




}
