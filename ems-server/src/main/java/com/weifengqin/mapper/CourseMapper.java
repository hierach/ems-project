package com.weifengqin.mapper;

import com.weifengqin.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 666
* @description 针对表【course】的数据库操作Mapper
* @createDate 2023-12-14 11:07:12
* @Entity com.weifengqin.entity.Course
*/
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 根据用户id，查询用户已经选的课程
     * @param userId
     * @return
     */
    List<Course> selectCourseByStudentId(@Param("userId") String userId);
}




