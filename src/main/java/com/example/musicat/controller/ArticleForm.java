package com.example.musicat.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

// 게시글 작성
@Getter @Setter
@NoArgsConstructor
public class ArticleForm {

    private int boardNo;
    private String subject;
    private String content;
}
