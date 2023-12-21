package com.weifengqin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weifengqin.entity.Cst;
import com.weifengqin.service.CstService;
import com.weifengqin.mapper.CstMapper;
import org.springframework.stereotype.Service;

/**
* @author 666
* @description 针对表【cst】的数据库操作Service实现
* @createDate 2023-12-14 11:07:18
*/
@Service
public class CstServiceImpl extends ServiceImpl<CstMapper, Cst>
    implements CstService{


    /**
     * 查询已选的课程号
     * @param userId
     * @return
     */
    @Override
    public String[] selectValidCourseNum(String userId) {
        CstMapper baseMapper = getBaseMapper();
        return baseMapper.selectValidCourseNum(userId);
    }
}




