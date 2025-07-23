package com.contacts.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDepartment {
    @JSONField(ordinal = 1)
    private String oldName;
    @JSONField(ordinal = 2)
    private String newName;
}
