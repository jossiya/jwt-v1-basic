package com.cos.jwt.controller;

import com.cos.jwt.dto.CustomResponse;
import com.cos.jwt.dto.JoinDto;
import com.cos.jwt.dto.LoginDto;
import com.cos.jwt.model.User;
import com.cos.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class RestApiController {
    private final UserService userService;

    @GetMapping("/home")
    public String home(){
        return "<h1>home</h1>";
    }
    @GetMapping("/api/user")
    public String user(){
        return "user";
    }
    @GetMapping("/api/admin")
    public String admin(){
        return "admin";
    }
    @PostMapping("/join")
    public CustomResponse join(@RequestBody JoinDto joinDto){
        return userService.joinUser(joinDto);
    }
    @PostMapping("/api/user/login")
    public CustomResponse<LoginDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response){
        System.out.println("http : "+response);
        System.out.println("loginDto = " + loginDto);
        return CustomResponse.success(loginDto);
    }
}
