package com.cos.jwt.fllter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyFIlter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req =(HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse)  response;
        String headerAuth = req.getHeader("Authorization");
        if(true){
            chain.doFilter(req,res);
        }
        else{
            System.out.println("인증 안됬슈");
        }
    }
}
