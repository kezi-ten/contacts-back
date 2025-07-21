package com.contacts.controllers;

import com.contacts.pojo.Emp;
import com.contacts.pojo.Result;
import com.contacts.service.Empservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


//操作日志表   redis
@CrossOrigin(origins = "http://localhost:8081")
@Slf4j
@RestController
public class EmpController {
    @Autowired
    private Empservice empService;


    @PostMapping("/employees")
    public Result getAllEmployees() {

        log.info("获取所有员工信息");
        List<Emp> empList = empService.getAllEmployees();
        return Result.success(empList);
    }

    @PostMapping("/update")
    public Result updateUserInfo(@RequestBody Emp emp) {
        boolean isUpdated = empService.updateUserInfo(emp);
        if (isUpdated) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }

    }
        @PostMapping("/addEmployee")
        public Result addEmployee(@RequestBody Emp emp) {
            return empService.addEmployee(emp);
        }
    @PostMapping("/deleteEmployee")
    public Result deleteEmployee(@RequestBody Map<String, String> payload) {
        String emp_id = payload.get("emp_id");
        return empService.deleteEmployee(emp_id);
    }
    @PostMapping("/updateEmployee")
    public Result updateEmployee(@RequestBody Emp emp) {
        return empService.updateEmployee(emp);
    }
}