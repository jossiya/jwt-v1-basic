package com.cos.jwt.service;

import com.cos.jwt.dto.CustomResponse;
import com.cos.jwt.dto.JoinDto;
import com.cos.jwt.model.User;
import com.cos.jwt.model.UserRoleEnum;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AOKWKK2/2KDOOGLLS/WPPLDOOSIFISKDLL";

    //회원가입
    @Transactional
    public CustomResponse<?> joinUser(JoinDto requestDto) {
        System.out.println(requestDto);

        //아이디 검증
        String username = requestDto.getUsername();
        System.out.println("123123"+username);

        User found = userRepository.findByUsername(username).orElseThrow(()->new NullPointerException("정보가 없습니다."));
        System.out.println("회원가입 디비 값"+found);

        Pattern pattern1 = Pattern.compile("^([a-zA-Z0-9]*)$");
        if (found!=null) {
            System.out.println("에러가 왜뜨니");
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }
        else if((!pattern1.matcher(username).matches() || username.length() < 4)) throw new IllegalArgumentException("형식에 맞지않는 닉네임입니다.");
        System.out.println("------------------------------------");
        //비밀번호 검증
        Pattern pattern2 = Pattern.compile("^([a-z0-9]*)$");
        String password = requestDto.getPassword();
        if(!pattern2.matcher(username).matches()||password.length() < 4 || password.length() > 32){
            throw new IllegalArgumentException("형식에 맞지않는 비밀번호입니다.");
        }if(!password.equals(requestDto.getPasswordConfirm()))throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");


        UserRoleEnum role = UserRoleEnum.USER;
        password = passwordEncoder.encode(password);
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            role = UserRoleEnum.ADMIN;
        }
        User user = new User(username, password, role);
       return CustomResponse.success(userRepository.save(user));
    }
}
