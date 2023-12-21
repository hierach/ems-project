package com.weifengqin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName student
 */
@TableName(value ="student")
@Data
public class Student implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学号
     */
    @TableField(value = "stu_num")
    private String stuNum;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 密码
     */
    @TableField(value = "name")
    private String name;


    /**
     * 类型。1表示管理员，2学生，3教师
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 性别
     */
    @TableField(value = "sex")
    private String sex;

    /**
     * 年级
     */
    @TableField(value = "year")
    private String year;

    /**
     * 专业
     */
    @TableField(value = "major")
    private String major;

    /**
     * 所需学分。默认15个学分
     */
    @TableField(value = "need_score")
    private Integer needScore;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}