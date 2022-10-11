package com.cos.jwt.auth;

import com.cos.jwt.dto.LoginDto;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;

//http://localhost:8080/login
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername!!!");
       User userEntity = userRepository.findByUsername(username).orElseThrow(()->new NullPointerException("유저정보가 일치하지 않습니다."));
            System.out.println("userEntity 디비값:" + userEntity);
            return new UserDetailsImpl(userEntity);
    }
}
