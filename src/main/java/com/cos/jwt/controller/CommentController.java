package com.cos.jwt.controller;

import com.cos.jwt.auth.UserDetailsImpl;
import com.cos.jwt.dto.request.CommentDto;
import com.cos.jwt.dto.response.CustomResponse;
import com.cos.jwt.service.CommentService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/api/auth/comment")
    public CustomResponse createComment(@RequestBody CommentDto commentDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.댓글등록(commentDto,userDetails);
    }

    @GetMapping("/api/comment/{id}")
    public CustomResponse comments(@PathVariable Long id){
        return commentService.댓글하나조회(id);
    }
    @GetMapping("/api/comments/{id}")
    public CustomResponse noticeCommentList(@PathVariable Long id){
        return commentService.댓글목록조회(id);
    }
    @PutMapping("/api/auth/comment/{id}")
    public CustomResponse commentUpdate(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestBody CommentDto commentDto){
        return commentService.댓글수정(id,userDetails,commentDto);
    }
    @DeleteMapping("/api/auth/comment/{id}")
    public CustomResponse commentDelete(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.댓글삭제(id,userDetails);
    }

}
