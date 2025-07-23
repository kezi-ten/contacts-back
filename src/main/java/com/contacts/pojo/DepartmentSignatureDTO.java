package com.contacts.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentSignatureDTO {
    private String name;
    private String supervisor_id;

}
