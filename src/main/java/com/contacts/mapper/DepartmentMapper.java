package com.contacts.mapper;

import com.contacts.pojo.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentMapper {
    void insert(Department department);
}