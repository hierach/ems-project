package com.weifengqin.vo;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author qin start
 * @create 2023-12-14-15:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseVo {


    /**
     * 课程id
     */
    private String courseNum;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 开课时间
     */
    private LocalDateTime startTime;

    /**
     * 结课时间
     */
    private LocalDateTime endTime;

    /**
     * 满分
     */
    private Integer fullMark;

    /**
     * 学分
     */
    private Integer score;

    /**
     * 选课人数
     */
    private Integer selPeople;

    /**
     * 最大选课人数
     */
    private Integer maxPeople;

    /**
     * 上课地点
     */
    private String place;

    /**
     * 上课星期几
     */
    private Integer selWeek;

    /**
     * 第几节课开始上课(默认有1-13节课)
     */
    private Integer selStart;

    /**
     * 第几节课下课
     */
    private Integer selEnd;


    /**
     * 有关开这门课的教师信息
     */
    private String techName;

    private String techNum;

    /**
     * 分数，可有可无
     */
    private Integer mark;
}
