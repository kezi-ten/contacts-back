package com.contacts.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendCaptchaEmail(String toEmail, String captcha) throws MessagingException;
    boolean verifyEmailCaptcha(String email, String captcha);
}
