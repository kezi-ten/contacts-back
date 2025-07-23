package com.contacts.service;

import com.contacts.utils.CaptchaUtil;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class CaptchaService {

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CAPTCHA_PREFIX = "captcha:";

    // 生成验证码
    public Map<String, String> generateCaptcha() {
        String captchaId = java.util.UUID.randomUUID().toString();
        String captchaText = kaptchaProducer.createText();
        BufferedImage captchaImage = kaptchaProducer.createImage(captchaText);

        String base64Image = "";
        try {
            base64Image = CaptchaUtil.encode(captchaImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 存入 Redis
        redisTemplate.opsForValue().set(CAPTCHA_PREFIX + captchaId, captchaText, Duration.ofMinutes(5));

        Map<String, String> result = new HashMap<>();
        result.put("captchaId", captchaId);
        result.put("image", base64Image);
        return result;
    }


    // 验证验证码
    public boolean verifyCaptcha(String captchaId, String userInput) {
        String key = CAPTCHA_PREFIX + captchaId;
        String storedCaptcha = redisTemplate.opsForValue().get(key);
        if (storedCaptcha != null && storedCaptcha.equalsIgnoreCase(userInput)) {
            redisTemplate.delete(key);
            return true;
        }
        ;
        log.info("验证码：{}", userInput);
        return key.equals(userInput);
    }
}
