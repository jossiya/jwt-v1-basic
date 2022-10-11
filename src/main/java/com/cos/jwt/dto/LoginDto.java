package com.cos.jwt.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginDto {

    @NotBlank(message = "아이디를 입력해 주세요")
    @Pattern(regexp ="[a-zA-Z0-9]{4,12}", message = "아이디 형식이 맞질 않습니다." )
    private String username;
    
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp ="[a-z0-9]{4,32}" , message = "비밀번호 형식이 맞질 않습니다." )
    private String password;
}
