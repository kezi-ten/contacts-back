package com.contacts.mapper;

import com.contacts.pojo.Department;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DepartmentMapper {
    @Insert("INSERT INTO department (name, supervisor_id) VALUES (#{name}, #{supervisor_id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Department department);

    @Update("UPDATE department SET supervisor_id = #{empId} WHERE name = #{departmentName}")
    int updateDepartmentSupervisor(@Param("empId") String empId,
                                   @Param("departmentName")  String departmentName);
}