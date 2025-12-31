package com.itemshare.dto;

import com.itemshare.entity.User;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private User user;
    
    public LoginResponseDTO(String token, User user) {
        this.token = token;
        this.user = user;
    }
}