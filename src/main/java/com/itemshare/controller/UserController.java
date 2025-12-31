package com.itemshare.controller;

import com.itemshare.dto.UserLoginDTO;
import com.itemshare.dto.UserRegisterDTO;
import com.itemshare.dto.LoginResponseDTO;
import com.itemshare.dto.UserResponseDTO;
import com.itemshare.dto.ApiResponse;
import com.itemshare.entity.User;
import com.itemshare.listener.UserEntityListener;
import com.itemshare.service.UserService;
import com.itemshare.utils.PasswordUtils;
import com.itemshare.utils.ValidationUtils;
import com.itemshare.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@RequestBody UserRegisterDTO userDTO) {
        try {
            // 验证用户名和邮箱是否已存在
            if (userService.existsByUsername(userDTO.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("用户名已存在", 409));
            }
            
            if (userService.existsByEmail(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("邮箱已存在", 409));
            }
            
            // 验证密码和确认密码
            if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("密码和确认密码不匹配", 400));
            }
            
            // 验证输入
            ValidationUtils.validateRegisterDTO(userDTO);
            
            // 创建新用户，密码将由UserEntityListener自动加密
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());  // 密码将由EntityListener自动加密
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setStatus(1);  // 默认激活状态
            
            User savedUser = userService.save(user);
            UserResponseDTO responseData = new UserResponseDTO(savedUser);
            return ResponseEntity.ok(ApiResponse.success(responseData, "注册成功"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), 400));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("注册过程中发生错误", 500));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<User>> getUsers(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.findAll(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<User>> searchUsers(@RequestParam String keyword,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.findByKeyword(keyword, pageable);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> existingUser = userService.findById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            User updatedUser = userService.save(user);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateUserStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            userService.updateUserStatus(id, 0); // 设置为禁用状态
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody UserLoginDTO loginDTO) {
        System.out.println("=== Login方法被调用 ===");
        System.out.println("接收到的登录请求: " + loginDTO.getUsername());
        try {
            // 验证输入
            System.out.println("开始验证输入...");
            ValidationUtils.validateLoginDTO(loginDTO);
            
            // 通过用户名或邮箱查找用户
            System.out.println("查找用户: " + loginDTO.getUsername());
            Optional<User> userOpt = userService.findByUsername(loginDTO.getUsername());
            if (!userOpt.isPresent()) {
                System.out.println("通过用户名未找到，尝试通过邮箱查找...");
                userOpt = userService.findByEmail(loginDTO.getUsername());
            }
            
            if (!userOpt.isPresent()) {
                System.out.println("用户未找到");
                return ResponseEntity.status(401).body(ApiResponse.error("用户名或密码不正确", 401));
            }
            
            User user = userOpt.get();
            System.out.println("找到用户: " + user.getUsername());
            System.out.println("数据库中的密码: " + user.getPassword());
            System.out.println("输入的密码: " + loginDTO.getPassword());
            
            // 打印详细的密码验证信息
            String inputPassword = loginDTO.getPassword();
            String storedPassword = user.getPassword();
            
            // 检查存储的密码格式
            boolean isBCryptPassword = storedPassword != null && storedPassword.startsWith("$2a$");
            System.out.println("存储的密码是否为BCrypt格式: " + isBCryptPassword);
            
            // 使用PasswordUtils验证密码
            boolean passwordMatches = PasswordUtils.matches(loginDTO.getPassword(), user.getPassword());
            System.out.println("密码验证结果: " + passwordMatches);
            
            if (passwordMatches) {
                System.out.println("密码验证成功！");
            } else {
                System.out.println("密码验证失败！");
            }
            
            if (!passwordMatches) {
                System.out.println("密码不匹配");
                return ResponseEntity.status(401).body(ApiResponse.error("用户名或密码不正确", 401));
            }
            
            // 更新最后登录时间
            System.out.println("更新登录时间...");
            userService.updateLastLoginTime(user.getId());
            
            // 生成令牌
            System.out.println("生成令牌...");
            String token = TokenUtils.generateToken(user);
            
            System.out.println("登录成功");
            LoginResponseDTO responseData = new LoginResponseDTO(token, user);
            return ResponseEntity.ok(ApiResponse.success(responseData, "登录成功"));
        } catch (IllegalArgumentException e) {
            System.out.println("输入验证错误: " + e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), 400));
        } catch (Exception e) {
            System.out.println("登录过程中发生错误: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("登录过程中发生错误", 500));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getCurrentUserProfile(@RequestHeader("Authorization") String authorization) {
        try {
            // 从token中提取用户信息
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("未授权访问", 401));
            }
            
            String token = authorization.substring(7); // 移除 "Bearer " 前缀
            String username = TokenUtils.getUsernameFromToken(token);
            
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("无效的令牌", 401));
            }
            
            Optional<User> userOpt = userService.findByUsername(username);
            if (!userOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("用户不存在", 404));
            }
            
            UserResponseDTO responseData = new UserResponseDTO(userOpt.get());
            return ResponseEntity.ok(ApiResponse.success(responseData, "获取用户信息成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("获取用户信息失败", 500));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateCurrentUserProfile(
            @RequestHeader("Authorization") String authorization,
            @RequestBody User userUpdate) {
        try {
            // 从token中提取用户信息
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("未授权访问", 401));
            }
            
            String token = authorization.substring(7); // 移除 "Bearer " 前缀
            String username = TokenUtils.getUsernameFromToken(token);
            
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("无效的令牌", 401));
            }
            
            Optional<User> userOpt = userService.findByUsername(username);
            if (!userOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("用户不存在", 404));
            }
            
            User currentUser = userOpt.get();
            
            // 更新允许的字段
            if (userUpdate.getNickname() != null) {
                currentUser.setNickname(userUpdate.getNickname());
            }
            if (userUpdate.getBio() != null) {
                currentUser.setBio(userUpdate.getBio());
            }
            if (userUpdate.getAvatar() != null) {
                currentUser.setAvatar(userUpdate.getAvatar());
            }
            if (userUpdate.getPhone() != null) {
                currentUser.setPhone(userUpdate.getPhone());
            }
            if (userUpdate.getLocation() != null) {
                currentUser.setLocation(userUpdate.getLocation());
            }
            
            currentUser.setUpdatedAt(LocalDateTime.now());
            
            User savedUser = userService.save(currentUser);
            UserResponseDTO responseData = new UserResponseDTO(savedUser);
            return ResponseEntity.ok(ApiResponse.success(responseData, "更新用户信息成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("更新用户信息失败", 500));
        }
    }
}
