package com.weifengqin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName course
 */
@TableName(value ="course")
@Data
public class Course implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 课程id
     */
    @TableField(value = "course_num")
    private String courseNum;

    /**
     * 课程名
     */
    @TableField(value = "course_name")
    private String courseName;

    /**
     * 开课时间
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /**
     * 结课时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /**
     * 满分
     */
    @TableField(value = "full_mark")
    private Integer fullMark;

    /**
     * 学分
     */
    @TableField(value = "score")
    private Integer score;

    /**
     * 选课人数
     */
    @TableField(value = "sel_people")
    private Integer selPeople;

    /**
     * 最大选课人数
     */
    @TableField(value = "max_people")
    private Integer maxPeople;

    /**
     * 上课地点
     */
    @TableField(value = "place")
    private String place;

    /**
     * 上课星期几
     */
    @TableField(value = "sel_week")
    private Integer selWeek;

    /**
     * 第几节课开始上课(默认有1-13节课)
     */
    @TableField(value = "sel_start")
    private Integer selStart;

    /**
     * 第几节课下课
     */
    @TableField(value = "sel_end")
    private Integer selEnd;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}