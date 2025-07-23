package com.contacts.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.contacts.pojo.Department;
import com.contacts.pojo.DepartmentSignatureDTO;
import com.contacts.pojo.Result;

import com.contacts.service.Empservice;
import com.contacts.utils.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/addDepartment")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class DepartmentController {

    @Autowired
    private Empservice departmentService;

    @PostMapping
    public Result addDepartment(@RequestBody Department department,
        @RequestHeader("X-Signature") String signature,
        @RequestHeader("X-Timestamp") String timestamp) {
            log.info("收到新增部门请求: department={}, signature={}, timestamp={}", department, signature, timestamp);

            try {
                // 构建签名 DTO
                DepartmentSignatureDTO dto = new DepartmentSignatureDTO();
                dto.setName(department.getName());
                dto.setSupervisor_id(department.getSupervisor_id());

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
        return departmentService.addDepartment(department);
    }
}
