package com.contacts.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.contacts.pojo.*;
import com.contacts.service.Empservice;
import com.contacts.utils.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result updateUserInfo(@RequestBody Emp emp,
                                 @RequestHeader("X-Signature") String signature,
                                 @RequestHeader("X-Timestamp") String timestamp) {
        log.info("收到更新个人信息请求: emp={},signature={}, timestamp={}",
                emp,  signature, timestamp);
        try {
            UpdateSignatureDTO dto = new UpdateSignatureDTO();
            dto.setEmp_id(emp.getEmp_id());
            dto.setPhone(emp.getPhone());
            dto.setEmail(emp.getEmail());
            String dataStr = JSON.toJSONString(dto); // 将 Emp 对象转换为 JSON 字符串
            boolean isValidSignature = SignatureUtil.verifySignature(dataStr, timestamp, signature);
            if (!isValidSignature) {
                log.error("签名验证错误");
                return Result.error("签名验证失败");
            }
        } catch (Exception e) {
            log.error("签名验证失败", e);
            return Result.error("签名验证失败");
        }
        log.info("签名认证成功");

        boolean isUpdated = empService.updateUserInfo(emp);
        if (isUpdated) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }

    }
        @PostMapping("/addEmployee")
        public Result addEmployee(@RequestBody Emp emp,
                                  @RequestHeader("X-Signature") String signature,
                                  @RequestHeader("X-Timestamp") String timestamp) {
        log.info("收到添加员工请求: emp={}, signature={}, timestamp={}", emp, signature, timestamp);
            try {
                // 构建签名 DTO
                AddEmployeeSignatureDTO dto = new AddEmployeeSignatureDTO();
                dto.setEmp_id(emp.getEmp_id());
                dto.setName(emp.getName());
                dto.setDepartment_id(emp.getDepartment_id());
                dto.setPosition(emp.getPosition());
                dto.setPassword(emp.getPassword());

                String dataStr = JSON.toJSONString(dto, SerializerFeature.SortField);
                boolean isValidSignature = SignatureUtil.verifySignature(dataStr, timestamp, signature);

                if (!isValidSignature) {
                    return Result.error("签名验证失败");
                }
            } catch (Exception e) {
                log.error("签名验证失败", e);
                return Result.error("签名验证失败");
            }
            log.info("签名认证成功");
            return empService.addEmployee(emp);
        }
    @PostMapping("/deleteEmployee")
    public Result deleteEmployee(@RequestBody Map<String, String> payload,
                                 @RequestHeader("X-Signature") String signature,
                                 @RequestHeader("X-Timestamp") String timestamp) {
        String emp_id = payload.get("emp_id");
        log.info("收到删除员工请求: emp_id={}, signature={}, timestamp={}", emp_id, signature, timestamp);

        try {
            // 构建签名 DTO
            DeleteEmployeeSignatureDTO dto = new DeleteEmployeeSignatureDTO();
            dto.setEmp_id(emp_id);

            String dataStr = JSON.toJSONString(dto);
            boolean isValidSignature = SignatureUtil.verifySignature(dataStr, timestamp, signature);

            if (!isValidSignature) {
                return Result.error("签名验证失败");
            }
        } catch (Exception e) {
            log.error("签名验证失败", e);
            return Result.error("签名验证失败");
        }

        log.info("签名认证成功");
        return empService.deleteEmployee(emp_id);
    }
    @PostMapping("/updateEmployee")
    public Result updateEmployee(@RequestBody Emp emp,
                                 @RequestHeader("X-Signature") String signature,
                                 @RequestHeader("X-Timestamp") String timestamp) {
        log.info("收到修改员工请求: emp={}, signature={}, timestamp={}", emp, signature, timestamp);
        try {
            // 构建签名 DTO
            AddEmployeeSignatureDTO dto = new AddEmployeeSignatureDTO();
            dto.setEmp_id(emp.getEmp_id());
            dto.setName(emp.getName());
            dto.setDepartment_id(emp.getDepartment_id());
            dto.setPosition(emp.getPosition());
            dto.setPassword(emp.getPassword());

            String dataStr = JSON.toJSONString(dto, SerializerFeature.SortField);
            boolean isValidSignature = SignatureUtil.verifySignature(dataStr, timestamp, signature);

            if (!isValidSignature) {
                return Result.error("签名验证失败");
            }
        } catch (Exception e) {
            log.error("签名验证失败", e);
            return Result.error("签名验证失败");
        }
        log.info("签名认证成功");
        return empService.updateEmployee(emp);
    }
    @PostMapping("/updateDepartment")
    public Result updateDepartment(@RequestBody UpdateDepartment updateDepartment,
                                   @RequestHeader("X-Signature") String signature,
                                   @RequestHeader("X-Timestamp") String timestamp) {
        String oldName = updateDepartment.getOldName();
        String newName = updateDepartment.getNewName();

        log.info("收到更新部门请求: oldName={}, newName={}, signature={}, timestamp={}", updateDepartment.getOldName(), updateDepartment.getNewName(),  signature, timestamp);

        try {


            String dataStr = JSON.toJSONString(updateDepartment);
            boolean isValidSignature = SignatureUtil.verifySignature(dataStr, timestamp, signature);

            if (!isValidSignature) {
                return Result.error("签名验证失败");
            }
        } catch (Exception e) {
            log.error("签名验证失败", e);
            return Result.error("签名验证失败");
        }

        log.info("签名认证成功");

        // 执行更新操作
        int rowsAffected = empService.updateDepartment(oldName, newName);
        if (rowsAffected > 0) {
            Map<String, String> resultData = Map.of("oldName", oldName, "newName", newName);
            return Result.success(resultData);
        } else {
            return Result.error("未找到匹配的部门名，更新失败");
        }
    }

}