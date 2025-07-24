package com.contacts.service.impl;

import com.contacts.mapper.DepartmentMapper;
import com.contacts.pojo.*;
import com.contacts.service.Empservice;
import com.contacts.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.contacts.mapper.EmpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class Empserviceimpl implements Empservice{

    @Autowired
    private EmpMapper empMapper; // 注入EmpMapper
    @Autowired
    private EmpMapper empLogMapper;
    @Autowired
    private DepartmentMapper DepartmentMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String EMP_KEY = "emp:list";
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
        // 先从 Redis 中获取数据
        List<Emp> empList = (List<Emp>) redisTemplate.opsForValue().get(EMP_KEY);

        if (empList == null) {
            // Redis 中没有数据，从数据库中获取
            log.info("缓存中无数据，从数据库中获取员工信息");
            empList = empMapper.list();

            if (empList != null && !empList.isEmpty()) {
                // 将数据存入 Redis，设置过期时间为 1 小时
                redisTemplate.opsForValue().set(EMP_KEY, empList, 1, TimeUnit.HOURS);
            }
        }
        log.info("从缓存中获取员工信息");
        return empList;
    }
    @Override
    public void insertLog(EmpLog empLog) {
        empLogMapper.insert(empLog);
    }
    @Transactional(rollbackFor = Exception.class)  //事务管理
    @Override
    public boolean updateUserInfo(Emp emp) {
        log.info("清空缓存数据");
        redisTemplate.delete(EMP_KEY);
        int rows = empMapper.updateUserInfo(emp.getEmp_id(), emp.getPhone(), emp.getEmail());
       // int a=1/0;
        EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "(id="+emp.getEmp_id()+", phone="+emp.getPhone()+", email="+emp.getEmail()+")");
        this.insertLog(empLog);
        return rows > 0;


    }
    @Transactional
    @Override
    public Result addDepartment(Department department) {
        log.info("清空缓存数据");
        redisTemplate.delete(EMP_KEY);
        if (department.getName() == null || department.getName().trim().isEmpty()) {
            return Result.error("部门名称不能为空");
        }

        // 1. 插入部门
        DepartmentMapper.insert(department);

        // 2. 更新主管员工的部门和职位
        if (department.getSupervisor_id() != null && !department.getSupervisor_id().trim().isEmpty()) {
            empMapper.updateEmployeeDepartmentAndPosition(department.getSupervisor_id(), department.getName());
        }

        return Result.success("新增部门并更新员工部门成功");
    }

    @Override
    public Result addEmployee(Emp emp) {
        log.info("清空缓存数据");
        redisTemplate.delete(EMP_KEY);

        empMapper.insertemp(emp);
        return Result.success("新增员工成功");}
    @Override
    public Result deleteEmployee(String emp_id) {
        log.info("清空缓存数据");
        redisTemplate.delete(EMP_KEY);
        int rows = empMapper.deleteById(emp_id);
        if (rows > 0) {
            return Result.success("删除员工成功");
        } else {
            return Result.error("删除员工失败，员工不存在");
        }
    }
    @Override
    public Result updateEmployee(Emp emp) {
        log.info("清空缓存数据");
        redisTemplate.delete(EMP_KEY);
        int rows = empMapper.update(emp);
        if ("部门总管".equals(emp.getPosition())) {
            if (emp.getDepartment_id() != null && !emp.getDepartment_id().isEmpty()) {
                DepartmentMapper.updateDepartmentSupervisor(emp.getEmp_id(), emp.getDepartment_id());
            }
        }


        if (rows > 0) {
            return Result.success("更新员工信息成功");
        } else {
            return Result.error("更新员工信息失败，员工不存在");
        }
    }
    @Override
    public int updateDepartment(String oldName, String newName) {
        log.info("清空缓存数据");
        redisTemplate.delete(EMP_KEY);
        int rowsAffected = 0;

        // 更新部门表
        int deptRows = empMapper.updateDepartmentName(oldName, newName);
        rowsAffected += deptRows;

        // 更新员工表中的部门名
        int empRows = empMapper.updateEmployeeDepartmentName(oldName, newName);
        rowsAffected += empRows;

        return rowsAffected;
    }
    @Override
    public List<Admin> getAllAdmins() {

        return empMapper.getAllAdmins();
    }

}