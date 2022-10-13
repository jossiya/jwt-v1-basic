package com.cos.jwt.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDto {
    private  Long noticeId;
    private String comment;

}
