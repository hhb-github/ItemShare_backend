package com.itemshare.utils;

public class TestPassword {
    public static void main(String[] args) {
        String password1 = "admin123";
        String password2 = "password123";
        
        String hash1 = PasswordUtils.encode(password1);
        String hash2 = PasswordUtils.encode(password2);
        
        System.out.println("密码: " + password1);
        System.out.println("BCrypt Hash: " + hash1);
        
        System.out.println("\n密码: " + password2);
        System.out.println("BCrypt Hash: " + hash2);
        
        boolean matches1 = PasswordUtils.matches(password1, hash1);
        boolean matches2 = PasswordUtils.matches(password2, hash2);
        
        System.out.println("\n验证结果:");
        System.out.println("admin123 验证: " + matches1);
        System.out.println("password123 验证: " + matches2);
    }
}