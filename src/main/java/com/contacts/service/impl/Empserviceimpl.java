package com.contacts.service.impl;

import com.contacts.mapper.DepartmentMapper;
import com.contacts.pojo.*;
import com.contacts.service.Empservice;
import com.contacts.utils.JwtUtils;
import org.springframework.stereotype.Service;
import com.contacts.mapper.EmpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Empserviceimpl implements Empservice{

    @Autowired
    private EmpMapper empMapper; // 注入EmpMapper
    @Autowired
    private EmpMapper empLogMapper;
    @Autowired
    private DepartmentMapper DepartmentMapper;
    @Override
    public LoginInfo login(Emp emp) {
        Emp empLogin = empMapper.getUsernameAndPassword(emp);
        if(empLogin != null){
            //1. 生成JWT令牌
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("id", empLogin.getEmp_id());
            dataMap.put("username", empLogin.getName());

            String jwt = JwtUtils.generateJwt(dataMap);
            LoginInfo loginInfo = new LoginInfo(empLogin.getEmp_id(), empLogin.getPhone(),empLogin.getName(), jwt);
            return loginInfo;
        }
        return null;
    }
    @Override
    public List<Emp> getAllEmployees() {
        return empMapper.list();
    }
    @Override
    public void insertLog(EmpLog empLog) {
        empLogMapper.insert(empLog);
    }
    @Transactional(rollbackFor = Exception.class)  //事务管理
    @Override
    public boolean updateUserInfo(Emp emp) {

        int rows = empMapper.updateUserInfo(emp.getEmp_id(), emp.getPhone(), emp.getEmail());
        //int a=1/0;
        EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "(id="+emp.getEmp_id()+", phone="+emp.getPhone()+", email="+emp.getEmail()+")");
        this.insertLog(empLog);
        return rows > 0;


    }
    @Override
    public Result addDepartment(Department department) {
        DepartmentMapper.insert(department);
        return Result.success("新增部门成功");
    }
    @Override
    public Result addEmployee(Emp emp) {

        empMapper.insertemp(emp);
        return Result.success("新增员工成功");}
    @Override
    public Result deleteEmployee(String emp_id) {
        int rows = empMapper.deleteById(emp_id);
        if (rows > 0) {
            return Result.success("删除员工成功");
        } else {
            return Result.error("删除员工失败，员工不存在");
        }
    }
    @Override
    public Result updateEmployee(Emp emp) {
        int rows = empMapper.update(emp);
        if (rows > 0) {
            return Result.success("更新员工信息成功");
        } else {
            return Result.error("更新员工信息失败，员工不存在");
        }
    }
}