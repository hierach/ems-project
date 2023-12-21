package com.weifengqin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author qin start
 * @create 2023-12-11-21:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {
    String token;

    String userId;

    String userName;
}
