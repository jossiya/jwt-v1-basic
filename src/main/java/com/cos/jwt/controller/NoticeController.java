package com.cos.jwt.controller;

import com.cos.jwt.auth.UserDetailsImpl;
import com.cos.jwt.dto.response.CustomResponse;
import com.cos.jwt.dto.request.NoticeDto;
import com.cos.jwt.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    @PostMapping("/api/auth/post")
    public CustomResponse<Object> createNotice(@RequestBody NoticeDto requestDto , HttpServletResponse response,
                                               @AuthenticationPrincipal UserDetailsImpl principal){
        return noticeService.saveNotice(requestDto,response,principal.getUser());
    }
    @GetMapping("/api/post")
    public CustomResponse noticeList(){
        return noticeService.findAlls();
    }
    @GetMapping("/api/post/{id}")
        public CustomResponse noticeOne(@PathVariable Long id){
            return noticeService.oneNotice(id);
    }
    @PutMapping("/api/auth/post/{id}")
    public CustomResponse noticeUpdate(@PathVariable Long id ,@RequestBody NoticeDto noticeDto ,@AuthenticationPrincipal UserDetailsImpl principal){
      return  noticeService.update(id,principal,noticeDto);
    }
    @DeleteMapping("/api/auth/post/{id}")
    public  CustomResponse noticeDelete(@PathVariable Long id,@AuthenticationPrincipal UserDetailsImpl principal){
        return noticeService.게시글_삭제해(id,principal);
    }
}
