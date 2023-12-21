package com.weifengqin.mapper;

import com.weifengqin.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weifengqin.vo.StuLeaveVo;
import com.weifengqin.vo.TimeTableVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 666
* @description 针对表【student】的数据库操作Mapper
* @createDate 2023-12-11 14:52:10
* @Entity com.weifengqin.entity.Student
*/
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    List<StuLeaveVo> getLeaveList(@Param("stuNum") String stuNum);

    List<Integer> getHasScores(@Param("stuNum") String stuNum);

    List<TimeTableVo> getTimeTableList(@Param("stuNum") String stuNum);
}




