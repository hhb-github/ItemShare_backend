package com.itemshare.listener;

import com.itemshare.entity.User;
import com.itemshare.utils.PasswordUtils;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class UserEntityListener {
    
    @PrePersist
    @PreUpdate
    public void handleUserPassword(User user) {
        System.out.println("=== UserEntityListener被调用 ===");
        System.out.println("原始密码: " + user.getPassword());
        System.out.println("用户ID: " + user.getId());
        System.out.println("用户名称: " + user.getUsername());
        
        // 如果密码不以BCrypt前缀开头，则进行加密
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            System.out.println("开始加密密码...");
            String encryptedPassword = PasswordUtils.encodePassword(user.getPassword());
            System.out.println("加密后密码: " + encryptedPassword);
            user.setPassword(encryptedPassword);
            System.out.println("密码已设置到用户实体");
        } else {
            System.out.println("密码已经加密过，无需再次加密");
        }
    }
}