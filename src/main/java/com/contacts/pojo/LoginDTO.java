package com.contacts.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @JSONField(ordinal = 1)
    private String emp_id; //ID
    @JSONField(ordinal = 2)
    private String password; //用户名
    @JSONField(ordinal = 3)
    private String email; //密码
    @JSONField(ordinal = 4)
    private String emailCaptcha; //department_id

}
