package com.contacts.service.impl;

import com.contacts.pojo.Emp;
import com.contacts.pojo.EmpLog;
import com.contacts.pojo.LoginInfo;
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
}