package com.weifengqin.service;

import com.weifengqin.entity.Cst;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 666
* @description 针对表【cst】的数据库操作Service
* @createDate 2023-12-14 11:07:18
*/
public interface CstService extends IService<Cst> {

    String[] selectValidCourseNum(String userId);
}
