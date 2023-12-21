package com.weifengqin.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qin start
 * @create 2023-12-11-15:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userNum;

    /**
     * 密码
     */
    private String password;

    /**
     * 类型。1表示管理员，2学生，3教师
     */
    private Integer type;

    private String name;
}
