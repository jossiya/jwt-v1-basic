package com.cos.jwt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String comment;
    @ManyToOne
    @JoinColumn(name="NOTICE_ID",nullable = false)
    private Notice notice;
    @ManyToOne
    @JoinColumn(name="USER_ID",nullable = false)
    private User user;
}
