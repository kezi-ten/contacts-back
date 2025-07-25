package com.contacts.service;

import com.contacts.pojo.*;

import java.util.List;

public interface Empservice {
    LoginInfo login(Emp emp);
    /**
     * 查询所有员工信息
     */
    List<Emp> getAllEmployees();
    boolean updateUserInfo(Emp emp);
    public void insertLog(EmpLog empLog);
    Result addEmployee(Emp emp);
    Result deleteEmployee(String emp_id);
    Result updateEmployee(Emp emp);
    Result addDepartment(Department department);
    int updateDepartment(String oldName, String newName);
    boolean checkUserIsAdmin(String token);

}
