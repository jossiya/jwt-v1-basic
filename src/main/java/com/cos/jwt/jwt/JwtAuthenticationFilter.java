package com.cos.jwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.auth.UserDetailsImpl;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;



//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
// /login 요청해서  username,password 전송하면(post)
//UsernamePasswordAuthenticationFilter 동작함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter 로그인");

        //1.username , password 받아서
        //2.정상인지 로그인 시드를 해봄. authenticationManager로 로그인 시도를 하면!!
        // UserDetailsServiceImpl가 호출 되고 loadUserByUsername이 실행됨 됨
        //3.UserDetailsImpl를 세션에 담고(권한 관리를 위해서
        //4.JTW토큰을 만들어서 응답해주면 됨.
        try {
            ObjectMapper om = new ObjectMapper();
            User user=om.readValue(request.getInputStream(),User.class);
//            System.out.println("user"+user);
            UsernamePasswordAuthenticationToken authenticationToken=
            new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
//            System.out.println("토큰" +authenticationToken);

            //UserDtailServiceImpl 의 loadUserByUsername이 실행 됨
            //wjdtkddlaus authentication가 리턴됨
            //DB에 있는 username 과 password가 일치한다.
            Authentication authentication=
                    authenticationManager.authenticate(authenticationToken);
//            System.out.println("???? : "+authentication);
//            UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
//            System.out.println("토큰 :??"+userDetails.getUser().getUsername());
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //attemptAuthentication 실행 후 인증이 정상으로 되었으면 successfulAuthentication 함수 실행
    //JWT 토큰을 만들어서 REQUEST요청한 사용자에게 JWT토큰일 response해주먄 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("id", userDetails.getUser().getId())
                .withClaim("username", userDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX+jwtToken);
    }
}
