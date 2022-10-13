package com.cos.jwt.controller;

import com.cos.jwt.dto.response.CustomResponse;
import com.cos.jwt.dto.request.JoinDto;
import com.cos.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}
