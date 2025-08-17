package com.contacts.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.contacts.pojo.*;
import com.contacts.service.Empservice;
import com.contacts.utils.CryptoUtil;
import com.contacts.utils.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.contacts.utils.CryptoUtil.decrypt;


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
        try {

            String jsonData = JSON.toJSONString(empList);
            String encryptedData = CryptoUtil.encrypt(jsonData);

            return Result.success(encryptedData);
        } catch (Exception e) {
            log.error("加密数据失败", e);
            return Result.error("加密数据失败");
        }
    }

    @PostMapping("/update")
    public Result updateUserInfo(@RequestBody Emp emp,
                                 @RequestHeader("X-Signature") String signature,
                                 @RequestHeader("X-Timestamp") String timestamp) {
        log.info("收到更新个人信息请求: emp={},signature={}, timestamp={}",
                emp,  signature, timestamp);
        try {

            String dataStr = JSON.toJSONString(emp,SerializerFeature.WriteMapNullValue, SerializerFeature.SortField);
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


                String dataStr = JSON.toJSONString(emp,SerializerFeature.WriteMapNullValue, SerializerFeature.SortField);
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
    public Result deleteEmployee(@RequestBody Emp emp,
                                 @RequestHeader("X-Signature") String signature,
                                 @RequestHeader("X-Timestamp") String timestamp) {
        String emp_id = emp.getEmp_id();
        log.info("收到删除员工请求: emp_id={}, signature={}, timestamp={}", emp_id, signature, timestamp);

        try {


            String dataStr = JSON.toJSONString(emp,SerializerFeature.WriteMapNullValue, SerializerFeature.SortField);
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
    public Result updateEmployee(@RequestBody Map<String, String> requestBody,
                                 @RequestHeader("X-Signature") String signature,
                                 @RequestHeader("X-Timestamp") String timestamp) throws Exception {
        log.info("收到修改员工请求: emp={}, signature={}, timestamp={}", requestBody, signature, timestamp);


            String decryptedPayload = CryptoUtil.decrypt(requestBody.get("encryptedPayload"));
            Emp emp = JSON.parseObject(decryptedPayload, Emp.class);
            log.info("解密后的emp: {}", emp);
            try {
            String dataStr = JSON.toJSONString(emp,SerializerFeature.WriteMapNullValue, SerializerFeature.SortField);
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

            String dataStr = JSON.toJSONString(updateDepartment,SerializerFeature.WriteMapNullValue, SerializerFeature.SortField);

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
    @PostMapping("/checkAdmin")
    public Result checkAdmin(@RequestHeader("Authorization") String token) {
        // 去除 Bearer 前缀
        token = token.replace("Bearer ", "");
        boolean isAdmin = empService.checkUserIsAdmin(token);
        log.info("用户是否为管理员: {}", isAdmin);
        return Result.success(isAdmin);
    }

}