package com.itemshare.utils;

import java.util.regex.Pattern;
import com.itemshare.dto.UserLoginDTO;
import com.itemshare.dto.UserRegisterDTO;

public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^1[3-9]\\d{9}$"
    );

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        String trimmed = username.trim();
        return trimmed.length() >= 3 && trimmed.length() <= 50;
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        return password.length() >= 6 && password.length() <= 20;
    }

    public static String validateRequired(String field, String value) {
        if (value == null || value.trim().isEmpty()) {
            return field + "不能为空";
        }
        return null;
    }

    public static void validateLoginDTO(UserLoginDTO loginDTO) {
        if (loginDTO == null) {
            throw new IllegalArgumentException("登录数据不能为空");
        }
        
        String usernameError = validateRequired("用户名或邮箱", loginDTO.getUsername());
        if (usernameError != null) {
            throw new IllegalArgumentException(usernameError);
        }
        
        String passwordError = validateRequired("密码", loginDTO.getPassword());
        if (passwordError != null) {
            throw new IllegalArgumentException(passwordError);
        }
        
        if (!isValidPassword(loginDTO.getPassword())) {
            throw new IllegalArgumentException("密码长度必须在6-20个字符之间");
        }
    }

    public static void validateRegisterDTO(UserRegisterDTO registerDTO) {
        if (registerDTO == null) {
            throw new IllegalArgumentException("注册数据不能为空");
        }
        
        String usernameError = validateRequired("用户名", registerDTO.getUsername());
        if (usernameError != null) {
            throw new IllegalArgumentException(usernameError);
        }
        
        String emailError = validateRequired("邮箱", registerDTO.getEmail());
        if (emailError != null) {
            throw new IllegalArgumentException(emailError);
        }
        
        String passwordError = validateRequired("密码", registerDTO.getPassword());
        if (passwordError != null) {
            throw new IllegalArgumentException(passwordError);
        }
        
        String confirmPasswordError = validateRequired("确认密码", registerDTO.getConfirmPassword());
        if (confirmPasswordError != null) {
            throw new IllegalArgumentException(confirmPasswordError);
        }
        
        if (!isValidUsername(registerDTO.getUsername())) {
            throw new IllegalArgumentException("用户名长度必须在3-50个字符之间");
        }
        
        if (!isValidEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("邮箱格式不正确");
        }
        
        if (!isValidPassword(registerDTO.getPassword())) {
            throw new IllegalArgumentException("密码长度必须在6-20个字符之间");
        }
    }
}
