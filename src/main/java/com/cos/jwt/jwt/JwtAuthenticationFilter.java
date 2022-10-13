package com.cos.jwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.auth.UserDetailsImpl;
import com.cos.jwt.dto.request.LoginDto;
import com.cos.jwt.model.Token;
import com.cos.jwt.repository.RefreshRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;


//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
// /login 요청해서  username,password 전송하면(post)
//UsernamePasswordAuthenticationFilter 동작함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final RefreshRepository refreshRepository;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
//    private final UserService userService;


    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter 로그인");

        //1.username , password 받아서
        //2.정상인지 로그인 시드를 해봄. authenticationManager로 로그인 시도를 하면!!
        // UserDetailsServiceImpl가 호출 되고 loadUserByUsername이 실행됨 됨
        //3.UserDetailsImpl를 세션에 담고(권한 관리를 위해서
        //4.JTW토큰을 만들어서 응답해주면 됨.
        ObjectMapper om = new ObjectMapper();
        LoginDto loginDto=null;
        try {
            loginDto=om.readValue(request.getInputStream(),LoginDto.class);
//            Pattern pattern1 = Pattern.compile("^([a-zA-Z0-9]*)$");
//            if(!pattern1.matcher(loginDto.getUsername()).matches() || loginDto.getUsername().length() < 4){
//                    throw new IllegalArgumentException("형식에 맞지않는 닉네임입니다.");
//                System.out.println(loginDto);
//            }
//            System.out.println("user"+user);
        } catch (IOException e) {
            System.out.println("눌값 에러입니다~");
            throw new RuntimeException(e);
        }
        System.out.println("제이슨 로그인 데이타"+loginDto);
//        userService.loginProcess(loginDto);
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(  loginDto.getUsername(),loginDto.getPassword());
            System.out.println("토큰" +authenticationToken);

        //UserDtailServiceImpl 의 loadUserByUsername이 실행 됨
        //wjdtkddlaus authentication가 리턴됨
        //DB에 있는 username 과 password가 일치한다.
        Authentication authentication=
                authenticationManager.authenticate(authenticationToken);
//            System.out.println("???? : "+authentication);
//            UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
//            System.out.println("토큰 :??"+userDetails.getUser().getUsername());
        return authentication;
    }
    //attemptAuthentication 실행 후 인증이 정상으로 되었으면 successfulAuthentication 함수 실행
    //JWT 토큰을 만들어서 REQUEST요청한 사용자에게 JWT토큰일 response해주먄 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();


        //어세스
        String jwtToken = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("id", userDetails.getUser().getId())
                .withClaim("username", userDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        //리프레시
        String refreshToken =  JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME2))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET2));
        Token token = Token.builder()
                .id(userDetails.getUser().getId())
                .user(userDetails.getUser())
                .value(refreshToken)
                .build();
        refreshRepository.save(token);

        response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX+jwtToken);
        response.addHeader(JwtProperties.HEADER_STRING2,JwtProperties.TOKEN_PREFIX+refreshToken);

//        chain.doFilter(request,response);
        try( PrintWriter writer = response.getWriter()) {

            JSONObject json = new JSONObject();
            json.put("success","true");
            json.put("username",userDetails.getUser().getUsername());
            json.put("password",userDetails.getUser().getPassword());


            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

            writer.write(json.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
