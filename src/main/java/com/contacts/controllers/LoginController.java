package com.contacts.controllers;
import com.contacts.pojo.Emp;
import com.contacts.pojo.Result;
import com.contacts.pojo.LoginInfo;
import com.contacts.service.CaptchaService;
import com.contacts.service.Empservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private Empservice empService;

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/login")
    public Result login(@RequestBody Emp emp, @RequestHeader("X-Captcha-Key") String captchaKey,
                        @RequestHeader("X-Captcha") String captchaValue){
        log.info("收到登录请求: emp={}, captchaKey={}, captchaValue={}", emp, captchaKey, captchaValue);
        boolean isValid = captchaService.verifyCaptcha(captchaKey, captchaValue);
        if (!isValid) {
            return Result.error("验证码错误或已过期");
        }
        log.info("员工首页 , {}", emp);
        LoginInfo loginInfo = empService.login(emp);
        if(loginInfo != null){
            return Result.success(loginInfo);
        }
        return Result.error("用户名或密码错误~");
    }


}
