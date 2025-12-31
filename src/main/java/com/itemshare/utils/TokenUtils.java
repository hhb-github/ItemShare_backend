package com.itemshare.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import com.itemshare.entity.User;

public class TokenUtils {
    
    public static String generateToken(User user) {
        String data = user.getId() + ":" + user.getUsername() + ":" + System.currentTimeMillis();
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
    
    public static String getUsernameFromToken(String token) {
        try {
            return parseTokenData(token, 1); // 获取用户名部分（索引1）
        } catch (Exception e) {
            return null;
        }
    }
    
    public static Long getUserIdFromToken(String token) {
        try {
            String userId = parseTokenData(token, 0); // 获取用户ID部分（索引0）
            return userId != null ? Long.parseLong(userId) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private static String parseTokenData(String token, int index) {
        try {
            String data = new String(Base64.getDecoder().decode(token));
            if (data != null && data.contains(":")) {
                String[] parts = data.split(":");
                if (parts.length > index) {
                    return parts[index]; // 返回指定索引的部分
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}