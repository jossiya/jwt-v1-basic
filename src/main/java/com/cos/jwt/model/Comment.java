package com.cos.jwt.model;

import com.cos.jwt.dto.request.CommentDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String comment;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="NOTICE_ID",nullable = false)
    private Notice notice;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="USER_ID",nullable = false)
    private User user;

   public Comment(Notice notice, String comment, User user){
       this.notice=notice;
       this.comment=comment;
       this.user=user;
   }
   public void update(CommentDto comment){
       this.comment=comment.getComment();
   }
}
