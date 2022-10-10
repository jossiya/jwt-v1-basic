package com.cos.jwt.jwt;

public interface JwtProperties {
    String SECRET= "ASJKSDJKJSFDSDJFJJSADJJJWIJIDFKSJKJKSJDAKDSLADA";
    int EXPIRATION_TIME = 60000*10;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING="Authorization";
}
