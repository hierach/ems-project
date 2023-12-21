package com.weifengqin.service;

import com.weifengqin.dto.Result;
import com.weifengqin.dto.StudentSelectCourseDto;
import com.weifengqin.dto.UserChangePasswordDto;
import com.weifengqin.dto.UserDto;
import com.weifengqin.entity.Cst;
import com.weifengqin.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 666
* @description 针对表【student】的数据库操作Service
* @createDate 2023-12-11 14:52:10
*/
public interface StudentService extends IService<Student> {

    Result login(UserDto userDto);

    Result getInfo(String userId);

    Result changePassword(UserChangePasswordDto userChangePasswordDto);

    Result getCourseList(String userId, String courseName);

    Result selectCourse(StudentSelectCourseDto selectCourseDto);

    Result getHasSelectCourseList(String userId,String courseName);

    Result delCourse(String userId, String courseNum, String techNum);

    Result getLeaveList(String stuNum);

    Result applyLeave(Cst cst);

    Result removeLeave(String courseNum, String techNum, String stuNum);

    Result getHasScores(String stuNum);

    Result getTimeTableList(String stuNum);
}
