package com.weifengqin.service;

import com.weifengqin.dto.Result;
import com.weifengqin.dto.UserChangePasswordDto;
import com.weifengqin.dto.UserDto;
import com.weifengqin.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.weifengqin.entity.Open;
import com.weifengqin.entity.Student;
import com.weifengqin.entity.Teacher;

/**
* @author 666
* @description 针对表【admin】的数据库操作Service
* @createDate 2023-12-11 16:46:17
*/
public interface AdminService extends IService<Admin> {

    Result login(UserDto userDto);

    Result getList();

    Result info(String userId);

    Result changePassword(UserChangePasswordDto changePasswordDto);

    Result getStudentList(String stuName);

    Result addStudent(Student student);

    Result editStudent(Student student);

    Result deleteStudent(String stuNum);

    Result getTeacherList(String name);

    Result addTeacher(Teacher teacher);

    Result editTeacher(Teacher teacher);

    Result deleteTeacher(String techNum);

    Result getWaitOpenList(String userId);

    Result permitCourse(Open open);

    Result deleteCourse(String adminNum,String courseNum,String techNum);
}
