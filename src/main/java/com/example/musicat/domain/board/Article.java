package com.example.musicat.domain.board;

import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "article")
public class Article {

    @Id @GeneratedValue
    @Column(name = "article_no")
    private Integer id;

    @Column(name = "member_no")
    private int memberNo;

    @Column(name = "board_no")
    private int boardNo;

    private String nickname; // 회원명
    private String subject; // 제목
    private String content; // 내용

    @Column(name = "writedate", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp writedate; // 작성일

    private int viewcount; // 조회수
    private int likecount; // 추천수


//    private FileVO attacheFile; //첨부 파일
//    private FileVO thumbnail; // 썸네일
//    private List<FileVO> fileList = new ArrayList<>(); // 이미지 List
//    private List<ReplyVO> replyList = new ArrayList<>(); // 댓글
//    private int likeCheck;


    //==생성 메소드==//
    public static Article createArticle(int memberNo, int boardNo, String nickname, String subject, String content) {
        Article article = new Article();
        article.memberNo = memberNo;
        article.boardNo = boardNo;
        article.nickname = nickname;
        article.subject = subject;
        article.content = content;
        return article;
    }

}
