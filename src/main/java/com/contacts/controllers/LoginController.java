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

import java.util.Map;

import static com.contacts.utils.CryptoUtil.decrypt;


@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private Empservice empService;

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> requestBody,
                        @RequestHeader("X-Captcha-Key") String captchaKey,
                        @RequestHeader("X-Captcha") String captchaValue,
                        @RequestHeader("X-Signature") String signature,
                        @RequestHeader("X-Timestamp") String timestamp) {
        log.info("收到登录请求: captchaKey={}, captchaValue={}, signature={}, timestamp={}",
                captchaKey, captchaValue, signature, timestamp);

        // 1. 验证验证码
        boolean isValid = captchaService.verifyCaptcha(captchaKey, captchaValue);
        if (!isValid) {
            return Result.error("验证码错误或已过期");
        }

        try {
            // 2. 获取并解密payload
            String encryptedPayload = requestBody.get("encryptedPayload");
            if (encryptedPayload == null || encryptedPayload.isEmpty()) {
                return Result.error("无效的请求参数");
            }
            String decryptedPayload = decrypt(encryptedPayload);  // 需要实现解密方法

            // 3. 验证签名
            boolean isValidSignature = SignatureUtil.verifySignature(decryptedPayload, timestamp, signature);
            if (!isValidSignature) {
                return Result.error("签名验证失败");
            }

            // 4. 解析解密后的JSON数据
            Emp emp = JSON.parseObject(decryptedPayload, Emp.class);

/*
            // 5. 验证邮箱验证码 (需要实现邮箱验证码验证逻辑)
            boolean isEmailCaptchaValid = verifyEmailCaptcha(emp.getEmail(), emp.getEmailCaptcha());
            if (!isEmailCaptchaValid) {
                return Result.error("邮箱验证码错误或已过期");
            }
*/
            log.info("员工登录 , {}", emp);
            LoginInfo loginInfo = empService.login(emp);
            if (loginInfo != null) {
                return Result.success(loginInfo);
            }
            return Result.error("用户名或密码错误~");

        } catch (Exception e) {
            log.error("登录处理失败", e);
            return Result.error("登录处理失败");
        }
    }


}
