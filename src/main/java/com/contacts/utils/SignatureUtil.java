package com.contacts.utils;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
@Slf4j
public class SignatureUtil {
    private static final String SECRET_KEY = "e8f3a7b2c9d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0"; // 与前端保持一致
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final int TIMESTAMP_TOLERANCE = 300;

    public static boolean verifySignature(String dataStr, String timestamp, String signature) throws Exception {
        // 验证时间戳是否合法
        long requestTime;
        try {
            requestTime = Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("非法的时间戳格式");
        }

        long currentTime = Instant.now().getEpochSecond(); // 当前时间秒级
        if (Math.abs(currentTime - requestTime) > TIMESTAMP_TOLERANCE) {
            throw new RuntimeException("时间戳已过期");
        }

        String dataToSign = dataStr + "|" + timestamp;
        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        mac.init(keySpec);
        byte[] result = mac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : result) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        String expectedSignature = hexString.toString();
        log.info("expectedSignature: {}", expectedSignature);
        return expectedSignature.equals(signature);
    }
}
