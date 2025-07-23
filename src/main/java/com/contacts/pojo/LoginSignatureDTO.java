package com.contacts.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginSignatureDTO {
    @JSONField(ordinal = 1)
    private String emp_id;
    @JSONField(ordinal = 2)
    private String password;
}
