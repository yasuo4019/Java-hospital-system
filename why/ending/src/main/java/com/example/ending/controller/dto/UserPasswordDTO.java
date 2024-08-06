package com.example.ending.controller.dto;

import lombok.Data;

@Data
public class UserPasswordDTO {
    private Long userId;
    private String username;
    private String password;
    private String newPassword;
}
