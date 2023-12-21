package com.weifengqin.mapper;

import com.weifengqin.entity.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weifengqin.vo.MarkVo;
import com.weifengqin.vo.TechLeaveVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 666
* @description 针对表【teacher】的数据库操作Mapper
* @createDate 2023-12-11 16:46:07
* @Entity com.weifengqin.entity.Teacher
*/
@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {

    List<MarkVo> getMarkList(@Param("techNum") String techNum, @Param("courseName") String courseName);

    List<TechLeaveVo> getLeaveList(@Param("techNum") String techNum);
}




