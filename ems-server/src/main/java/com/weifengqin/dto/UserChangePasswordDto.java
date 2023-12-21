package com.weifengqin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qin start
 * @create 2023-12-13-23:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChangePasswordDto {
    private String userId;
    private String password;
}
