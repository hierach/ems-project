package com.weifengqin.mapper;

import com.weifengqin.entity.Cst;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 666
* @description 针对表【cst】的数据库操作Mapper
* @createDate 2023-12-14 11:07:18
* @Entity com.weifengqin.entity.Cst
*/
@Mapper
public interface CstMapper extends BaseMapper<Cst> {

    String[] selectValidCourseNum(String userId);

    List<String> getCourseNumListByTechNum(@Param("techNum") String techNum);
}




