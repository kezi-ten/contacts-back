package com.contacts.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSignatureDTO {
    @JSONField(ordinal = 1)
    private String emp_id; //ID
    @JSONField(ordinal = 2)
    private String phone;
    @JSONField(ordinal = 3)
    private String email;

}
