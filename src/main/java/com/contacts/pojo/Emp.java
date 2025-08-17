package com.contacts.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emp{
    @JSONField(ordinal = 1)
    private String emp_id; //ID
    @JSONField(ordinal = 2)
    private String name; //用户名
    @JSONField(ordinal = 3)
    private String phone; //密码
    @JSONField(ordinal = 4)
    private String department_id; //department_id
    @JSONField(ordinal = 5)
    private String position; //年龄
    @JSONField(ordinal = 6)
    private String password;
    @JSONField(ordinal = 7)
    private String email;


}
