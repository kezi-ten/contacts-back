package com.contacts.service.impl;

import com.contacts.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class EmailServiceimpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void sendCaptchaEmail(String toEmail, String captcha) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("登录验证码");
        helper.setText(String.format("您的登录验证码为: %s，有效期5分钟。", captcha), false);
        mailSender.send(message);
    }

    @Override
    public boolean verifyEmailCaptcha(String email, String captcha) {
        if (email == null || captcha == null) {
            return false;
        }
        String storedCaptcha = redisTemplate.opsForValue().get("email:captcha:" + email);
        log.info("验证邮箱验证码: email={}, inputCaptcha={}, storedCaptcha={}", email, captcha, storedCaptcha);
        return captcha.equals(storedCaptcha);
    }
}