package com.weifengqin.controller;

import com.weifengqin.dto.Result;
import com.weifengqin.dto.UserDto;
import com.weifengqin.service.AdminService;
import com.weifengqin.service.StudentService;
import com.weifengqin.service.TeacherService;
import com.weifengqin.utils.RedisConstants;
import com.weifengqin.utils.SystemConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author qin start
 * @create 2023-12-11-14:32
 */
@RestController
@Api
public class LoginController {

    @Resource
    private StudentService studentService;

    @Resource
    private AdminService adminService;

    @Resource
    private TeacherService teacherService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/login")
    @ApiOperation(value = "登录接口")
    public Result login(@RequestBody UserDto userDto){


        //三路选择登录
        if(userDto.getType().equals(SystemConstants.STUDENT_TYPE)){
            return studentService.login(userDto);
        }else if(userDto.getType().equals(SystemConstants.ADMIN_TYPE)){
            return adminService.login(userDto);
        }else{
            return teacherService.login(userDto);
        }
    }

    @DeleteMapping("/logout")
    public Result logout(String token){

//        System.out.println("删除token:" + token);
        String key = RedisConstants.LOGIN_USER_KEY + token;

        //删除redis中的token
        stringRedisTemplate.delete(key);

        return Result.ok();
    }
}
