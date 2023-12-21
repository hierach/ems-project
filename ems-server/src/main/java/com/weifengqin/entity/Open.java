package com.weifengqin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName open
 */
@TableName(value ="open")
@Data
public class Open implements Serializable {
    /**
     * 
     */
    @TableField(value = "admin_num")
    private String adminNum;

    /**
     * 
     */
    @TableField(value = "course_num")
    private String courseNum;

    /**
     * 
     */
    @TableField(value = "tech_num")
    private String techNum;

    /**
     * 
     */
    @TableField(value = "course_name")
    private String courseName;

    /**
     * 批准状态。0表示未批准，1表示批准
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 
     */
    @TableField(value = "score")
    private Integer score;

    /**
     * 
     */
    @TableField(value = "full_mark")
    private Integer fullMark;

    /**
     * 选课最大人数
     */
    @TableField(value = "max_people")
    private Integer maxPeople;

    /**
     * 
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 地点
     */
    @TableField(value = "place")
    private String place;

    /**
     * 选择星期几上课
     */
    @TableField(value = "sel_week")
    private Integer selWeek;

    /**
     * 上课开始时间(1-13可选)
     */
    @TableField(value = "sel_start")
    private Integer selStart;

    /**
     * 下课时间
     */
    @TableField(value = "sel_end")
    private Integer selEnd;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}