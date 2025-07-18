package com.contacts.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    private String emp_id;
    private String username;
    private String password;
    private String token; //令牌

}
