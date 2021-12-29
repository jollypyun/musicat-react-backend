package com.example.musicat.domain.board;

import com.example.musicat.controller.ArticleForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Alias("articleVo")
public class ArticleVO {

    private int no;
    private int memberNo;
    private int boardNo;
    private String nickname; // 회원명
    private String subject; // 제목
    private String content; // 내용
    private String writedate; // 작성일
    private int viewcount; // 조회수
    private int likecount; // 추천수
    private int displayNotice;
    private List<FileVO> fileList = new ArrayList<>(); // 이미지 List
    private List<ReplyVO> replyList = new ArrayList<>(); // 댓글
    private FileVO attacheFile; //첨부 파일
    private FileVO thumbnail; // 썸네일
//
    private int likeCheck;


    //==생성 메소드==//
    public static ArticleVO createArticle(int memberNo, String nickname, ArticleForm form) {
        ArticleVO articleVO = new ArticleVO();
        articleVO.memberNo = memberNo;
        articleVO.boardNo = form.getBoardNo();
        articleVO.nickname = nickname;
        articleVO.subject = form.getSubject();
        articleVO.content = form.getContent();
        return articleVO;
    }


    //==비즈니스 로직==//
    //게시글 수정
    public static void updateArticle(ArticleVO articleVO, BoardVO board , ArticleForm form) {
        articleVO.boardNo = form.getBoardNo();
        articleVO.subject = form.getSubject();
        articleVO.content = form.getContent();
    }

    public static ArticleVO addReplyAndLike(ArticleVO article, int likeCheck, List<ReplyVO> replys, int totalCount) {
        article.setLikeCheck(likeCheck);
        article.setReplyList(replys);
        article.setLikecount(totalCount);
        return article;
    }


    //조회수 증가
    public void addViewcount(){
        this.viewcount += 1;
    }


    @Override
    public String toString() {
        return "Article{" +
                "no=" + no +
                ", memberNo=" + memberNo +
                ", boardNo=" + boardNo +
                ", nickname='" + nickname + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", writedate='" + writedate + '\'' +
                ", viewcount=" + viewcount +
                ", likecount=" + likecount +
                ", displayNotice=" + displayNotice +
                ", fileList=" + fileList +
                ", replyList=" + replyList +
                ", likeCheck=" + likeCheck +
                '}';
    }

    public ArticleVO() {
    }

}
