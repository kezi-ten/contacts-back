package com.contacts.controllers;
import com.alibaba.fastjson.JSONObject;
import com.contacts.pojo.*;
import com.contacts.service.CaptchaService;
import com.contacts.service.EmailService;
import com.contacts.service.Empservice;
import com.contacts.utils.SignatureUtil;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSON;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.contacts.utils.CryptoUtil.decrypt;


@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private Empservice empService;

    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
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
            LoginDTO loginDTO =JSON.parseObject(decryptedPayload, LoginDTO.class);

            // 5. 验证邮箱验证码
            boolean isEmailCaptchaValid = emailService.verifyEmailCaptcha(loginDTO.getEmail(),loginDTO.getEmailCaptcha());
            if (!isEmailCaptchaValid) {
                return Result.error("邮箱验证码错误或已过期");
            }

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
    @PostMapping("/sendEmailCaptcha")
    public Result sendEmailCaptcha(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        log.info("发送邮箱验证码请求: {}", email);

        // 邮箱格式验证
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") ) {
            return Result.error("无效的邮箱格式");
        }

        // 生成6位数字验证码
        String captcha = String.format("%06d", new Random().nextInt(999999));

        try {
            // 存储验证码到Redis，设置5分钟过期
            redisTemplate.opsForValue().set("email:captcha:" + email, captcha, 5, TimeUnit.MINUTES);
            // 发送邮件
            emailService.sendCaptchaEmail(email, captcha);
            return Result.success("验证码已发送至邮箱");
        } catch (MessagingException | IllegalArgumentException e) {
            log.error("发送邮件失败", e);
            return Result.error("发送验证码失败，请稍后重试");
        }
    }


}
