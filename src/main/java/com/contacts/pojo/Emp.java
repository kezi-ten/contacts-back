package com.contacts.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emp{

    private String emp_id; //ID
    private String name; //用户名
    private String phone; //密码
    private String department_id; //department_id
    private String position; //年龄
    private String password;
    private String email;


}
