package com.itemshare.controller;

import com.itemshare.entity.User;
import com.itemshare.service.UserService;
import com.itemshare.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/temp")
public class TempUpdateController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/update-admin-password")
    public String updateAdminPassword() {
        // 生成新的BCrypt密码哈希
        String newPasswordHash = PasswordUtils.encode("admin123");
        
        Optional<User> adminOpt = userService.findByUsername("admin");
        if (!adminOpt.isPresent()) {
            return "管理员用户未找到";
        }
        
        User admin = adminOpt.get();
        admin.setPassword(newPasswordHash);
        userService.save(admin);
        
        return "管理员密码已更新为: " + newPasswordHash;
    }
    
    @GetMapping("/test-login")
    public String testLogin() {
        Optional<User> adminOpt = userService.findByUsername("admin");
        if (!adminOpt.isPresent()) {
            return "管理员用户未找到";
        }
        
        User admin = adminOpt.get();
        boolean matches = PasswordUtils.matches("admin123", admin.getPassword());
        
        return "密码验证结果: " + matches + ", 数据库密码: " + admin.getPassword();
    }
}