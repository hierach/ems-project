package com.weifengqin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName cst
 */
@TableName(value ="cst")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cst implements Serializable {
    /**
     * 
     */
    @TableField(value = "course_num")
    private String courseNum;

    /**
     * 
     */
    @TableField(value = "stu_num")
    private String stuNum;

    /**
     * 
     */
    @TableField(value = "tech_num")
    private String techNum;

    /**
     * 分数，当分数为-1的时候表示还未打分
     */
    @TableField(value = "mark")
    private Integer mark;

    @TableField(value = "leave_start")
    private LocalDateTime leaveStart;

    @TableField(value = "leave_end")
    private LocalDateTime leaveEnd;

    /**
     * 请假状态。
     * 0表示未申请，1表示申请了请假等待状态，-1表示不同意请假要求，2表示申请通过
     */
    @TableField(value = "leave_status")
    private Integer leaveStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Cst(String courseNum, String userId, String techNum) {
        this.courseNum = courseNum;
        this.stuNum = userId;
        this.techNum = techNum;
    }
}