package com.contacts.controllers;
import com.contacts.pojo.Emp;
import com.contacts.pojo.Result;
import com.contacts.pojo.LoginInfo;
import com.contacts.service.Empservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private Empservice empService;


    @PostMapping("/login")
    public Result login(@RequestBody Emp emp){
        log.info("员工首页 , {}", emp);
        LoginInfo loginInfo = empService.login(emp);
        if(loginInfo != null){
            return Result.success(loginInfo);
        }
        return Result.error("用户名或密码错误~");
    }


}
