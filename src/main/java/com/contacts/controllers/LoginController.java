package com.contacts.controllers;
import com.contacts.pojo.Emp;
import com.contacts.pojo.LoginSignatureDTO;
import com.contacts.pojo.Result;
import com.contacts.pojo.LoginInfo;
import com.contacts.service.CaptchaService;
import com.contacts.service.Empservice;
import com.contacts.utils.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSON;



@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private Empservice empService;

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/login")
    public Result login(@RequestBody Emp emp,
                        @RequestHeader("X-Captcha-Key") String captchaKey,
                        @RequestHeader("X-Captcha") String captchaValue,
                        @RequestHeader("X-Signature") String signature,
                        @RequestHeader("X-Timestamp") String timestamp){
        log.info("收到登录请求: emp={}, captchaKey={}, captchaValue={}, signature={}, timestamp={}",
                emp, captchaKey, captchaValue, signature, timestamp);
        boolean isValid = captchaService.verifyCaptcha(captchaKey, captchaValue);
        if (!isValid) {
            return Result.error("验证码错误或已过期");
        }
        try {
            LoginSignatureDTO dto = new LoginSignatureDTO();
            dto.setEmp_id(emp.getEmp_id());
            dto.setPassword(emp.getPassword());
            String dataStr = JSON.toJSONString(dto); // 将 Emp 对象转换为 JSON 字符串
            boolean isValidSignature = SignatureUtil.verifySignature(dataStr, timestamp, signature);
            if (!isValidSignature) {
                return Result.error("签名验证失败");
            }
        } catch (Exception e) {
            log.error("签名验证失败", e);
            return Result.error("签名验证失败");
        }
        log.info("签名认证成功");
        log.info("员工首页 , {}", emp);
        LoginInfo loginInfo = empService.login(emp);
        if(loginInfo != null){
            return Result.success(loginInfo);
        }
        return Result.error("用户名或密码错误~");
    }


}
