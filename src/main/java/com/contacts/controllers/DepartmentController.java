package com.contacts.controllers;

import com.contacts.pojo.Department;
import com.contacts.pojo.Result;

import com.contacts.service.Empservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addDepartment")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class DepartmentController {

    @Autowired
    private Empservice departmentService;

    @PostMapping
    public Result addDepartment(@RequestBody Department department) {
        return departmentService.addDepartment(department);
    }
}
