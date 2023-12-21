package com.weifengqin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName teacher
 */
@TableName(value ="teacher")
@Data
public class Teacher implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 教师号
     */
    @TableField(value = "tech_num")
    private String techNum;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 类型。默认为3
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 性别
     */
    @TableField(value = "sex")
    private String sex;

    /**
     * 学院
     */
    @TableField(value = "faculty")
    private String faculty;

    /**
     * 职称
     */
    @TableField(value = "job_title")
    private String jobTitle;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}