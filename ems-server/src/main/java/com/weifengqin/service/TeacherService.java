package com.weifengqin.service;

import com.weifengqin.dto.Result;
import com.weifengqin.dto.UserChangePasswordDto;
import com.weifengqin.dto.UserDto;
import com.weifengqin.entity.Cst;
import com.weifengqin.entity.Open;
import com.weifengqin.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import org.yaml.snakeyaml.error.Mark;

/**
* @author 666
* @description 针对表【teacher】的数据库操作Service
* @createDate 2023-12-11 16:46:07
*/
public interface TeacherService extends IService<Teacher> {

    Result login(UserDto userDto);

    Result getInfo(String userId);

    Result changePassword(UserChangePasswordDto changePasswordDto);

    Result getOpenList(String userId,String courseName);

    Result openCourse(Open open);

    Result editCourse(Open open);

    Result deleteCourse(String techNum, String courseNum, String adminNum);

    Result getMarkList(String techNum, String courseName);

    Result markOperate(Cst cst);

    Result getLeaveList(String techNum);

    Result allowApplyLeave(Cst cst);

    Result rejectApplyLeave(String courseNum, String techNum, String stuNum);
}
