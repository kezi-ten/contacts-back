package com.contacts.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddEmployeeSignatureDTO {
    @JSONField(ordinal = 1)
    private String emp_id;
    @JSONField(ordinal = 2)
    private String name;
    @JSONField(ordinal = 3)
    private String department_id;
    @JSONField(ordinal = 4)
    private String position;
    @JSONField(ordinal = 5)
    private String password;
}
