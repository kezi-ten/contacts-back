package com.contacts.controllers;
import com.contacts.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/captcha")
    public Map<String, String> getCaptcha() {
        return captchaService.generateCaptcha();
    }
}
