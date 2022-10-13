package com.cos.jwt.config;


import com.cos.jwt.fllter.MyFIlter;
import com.cos.jwt.jwt.JwtAuthenticationFilter;
import com.cos.jwt.jwt.JwtAuthorizationFilter;
import com.cos.jwt.repository.RefreshRepository;
import com.cos.jwt.repository.UserRepository;
import com.cos.jwt.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity//스프링 시큐리티 필터()가 스프링 필어체인에 등록이 된다.
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class SecurityConfig {
    private final CorsFilter corsFilter;
    private final UserRepository userRepository;
    private final RefreshRepository refreshRepository;
//    private final UserService userService;
    @Bean//해당 메서드의 리턴되는 오브젝트를 ioc로 등록해준다
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF protection 을 비활성화
        http.csrf().disable(); // csrf 보안 토큰 disable처리.
        http
        .exceptionHandling()
//                .accessDeniedHandler()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않겠다.
        .and()
        .httpBasic().disable() // rest api 만을 고려하여 기본 인증 설정은 해제하겠다.
        .formLogin().disable()//폼로그인 사용하지 않는다!
        .apply(new MyCustomDsl())
        .and()
        .authorizeRequests() // 요청에 대한 사용권한 체크
        .antMatchers("/api/admin/**").hasRole("ADMIN")
        .antMatchers("/api/user/**").hasAnyRole("USER","ADMIN")
        .anyRequest().permitAll();//나머지는 모두 사용 가능.

        return http.build();
    }
    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsFilter)//코스 정책 무시 하는 필터  번외 :@CrossOrigin(인증 X을 떄만) 인증이 필요하면 필터에 등록해서 인증해야함.
                    .addFilter(new JwtAuthenticationFilter(authenticationManager,refreshRepository))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager,userRepository));
        }
    }
}
