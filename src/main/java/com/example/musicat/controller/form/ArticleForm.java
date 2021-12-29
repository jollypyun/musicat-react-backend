package com.example.musicat.controller.form;


import com.example.musicat.domain.board.ArticleVO;
import com.example.musicat.domain.board.BoardVO;
import com.example.musicat.domain.board.FileVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

// 게시글 작성

@Getter
@Setter
//@NoArgsConstructor
public class ArticleForm {

    @NotNull(message = "게시판을 선택해 주세요")
    private int boardNo;
    @NotBlank(message = "제목을 입력해 주세요")
    private String subject;
    @NotBlank(message = "내용을 입력해 주세요")
    private String content;

    private List<BoardVO> boardList = new ArrayList<>(); //작성 게시글 목록
    private int displayNotice; //공지 설정


    private Long fileNo;
    private MultipartFile importAttacheFile;
    private List<MultipartFile> imageFiles;

    private FileVO attacheFile;
    private List<FileVO> fileList = new ArrayList<>();


    public static ArticleForm updateArticle(ArticleVO article){
        ArticleForm form = new ArticleForm();
        form.setSubject(article.getSubject());
        form.setBoardNo(article.getBoardNo());
        form.setContent(article.getContent());
        form.setDisplayNotice(article.getDisplayNotice());
        return form;
    }


    public ArticleForm() {
    }


}
