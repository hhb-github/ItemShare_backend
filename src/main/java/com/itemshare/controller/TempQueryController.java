package com.itemshare.controller;

import com.itemshare.entity.User;
import com.itemshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/temp")
public class TempQueryController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/user-password")
    public String getUserPassword() {
        Optional<User> user = userService.findByUsername("tempuser");
        if (user.isPresent()) {
            return "tempuser密码: " + user.get().getPassword();
        } else {
            return "用户未找到";
        }
    }
    
    @GetMapping("/admin-password")
    public String getAdminPassword() {
        Optional<User> user = userService.findByUsername("admin");
        if (user.isPresent()) {
            return "admin密码: " + user.get().getPassword();
        } else {
            return "管理员用户未找到";
        }
    }
}