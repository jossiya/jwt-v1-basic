package com.cos.jwt.model;

import com.cos.jwt.dto.request.NoticeDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Notice extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="USER_ID" , nullable = false)
    private User user;

    @OneToMany(mappedBy = "notice",fetch = FetchType.EAGER ,cascade = CascadeType.ALL)//lazy; mappedBy 연관관계의 주인이 아니다( 난 FK가 아니다) DB에 컬럼을 만들지 마세요.
    private List<Comment> comment; //db 기본적인 제약조건이 있는데 속성값이 얼래 원자값  1개 못들어간다고

    public Notice(NoticeDto noticeDto){
        this.title=noticeDto.getTitle();
        this.content=noticeDto.getContent();
    }
    public void update(NoticeDto noticeDto){
        this.title=noticeDto.getTitle();
        this.content=noticeDto.getContent();
    }

 }
