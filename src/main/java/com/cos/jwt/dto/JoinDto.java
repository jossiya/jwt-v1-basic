package com.cos.jwt.dto;

import com.cos.jwt.model.User;
import lombok.Data;

@Data
public class JoinDto {
    private String username;
    private String password;
    private String passwordConfirm;
    private boolean admin = false;
    private String adminToken = "";

}
