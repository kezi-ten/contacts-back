package com.contacts.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CryptoUtil {
    // 与前端保持一致的密钥（16字节，AES-128）
    private static final String SECRET_KEY = "0123456789abcdef";
    // 与前端保持一致的偏移量（16字节）
    private static final String IV = "abcdef9876543210";
    /**
     * AES-CBC加密（与前端解密逻辑对应）
     */
    public static String encrypt(String plaintext) throws Exception {
        // 1. 初始化密钥和偏移量（与解密共用同一套密钥和IV）
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

        // 2. 初始化解密器为加密模式（算法/模式/填充需与解密完全一致）
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        // 3. 执行加密（明文转字节 -> 加密 -> Base64编码输出，与前端解密流程逆向）
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = cipher.doFinal(plaintextBytes);

        // 4. 加密结果Base64编码（前端需用Base64解码后再解密）
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    /**
     * AES-CBC解密（匹配前端CryptoJS实现）
     */
    public static String decrypt(String ciphertext) throws Exception {
        // 1. 初始化密钥和偏移量
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

        // 2. 初始化CBC模式解密器（Pkcs7填充对应Java的PKCS5Padding）
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // 3. 解密（前端encrypted.toString()返回Base64编码，需先解码）
        byte[] encryptedBytes = Base64.getDecoder().decode(ciphertext);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // 4. 转换为UTF-8字符串
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
