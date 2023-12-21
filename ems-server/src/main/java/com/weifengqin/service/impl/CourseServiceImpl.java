package com.weifengqin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weifengqin.entity.Course;
import com.weifengqin.service.CourseService;
import com.weifengqin.mapper.CourseMapper;
import org.springframework.stereotype.Service;

/**
* @author 666
* @description 针对表【course】的数据库操作Service实现
* @createDate 2023-12-14 11:07:12
*/
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
    implements CourseService{

}




