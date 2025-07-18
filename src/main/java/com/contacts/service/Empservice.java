package com.contacts.service;

import com.contacts.pojo.Emp;
import com.contacts.pojo.EmpLog;
import com.contacts.pojo.LoginInfo;

import java.util.List;

public interface Empservice {
    LoginInfo login(Emp emp);
    /**
     * 查询所有员工信息
     */
    List<Emp> getAllEmployees();
    boolean updateUserInfo(Emp emp);
    public void insertLog(EmpLog empLog);
}
