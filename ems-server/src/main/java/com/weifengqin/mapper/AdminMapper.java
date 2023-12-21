package com.weifengqin.mapper;

import com.weifengqin.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 666
* @description 针对表【admin】的数据库操作Mapper
* @createDate 2023-12-11 16:46:17
* @Entity com.weifengqin.entity.Admin
*/
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

}




