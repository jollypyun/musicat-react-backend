package com.example.musicat.controller;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.web.multipart.MultipartFile;
import com.example.musicat.domain.board.Board;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

// 게시글 작성

//@Getter @Setter
//@NoArgsConstructor
public class ArticleForm {

    @NotNull(message = "게시판을 선택해 주세요")
    private int boardNo;
    @NotBlank(message = "제목을 입력해 주세요")
    private String subject;
    @NotBlank(message = "내용을 입력해 주세요")
    private String content;

    private List<ArticleController.BoardEx> boardList = new ArrayList<>(); //작성 게시글 목록
//    private List<Board> boardList = new ArrayList<>(); //작성 게시글 목록
    private int displayNotice; //공지 설정


    private Long fileNo;
    private MultipartFile importAttacheFile;
    private List<MultipartFile> imageFiles;


    public ArticleForm() {
    }

    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ArticleController.BoardEx> getBoardList() {
        return boardList;
    }

    public void setBoardList(List<ArticleController.BoardEx> boardList) {
        this.boardList = boardList;
    }

    public int getDisplayNotice() {
        return displayNotice;
    }

    public void setDisplayNotice(int displayNotice) {
        this.displayNotice = displayNotice;
    }

    public Long getFileNo() {
        return fileNo;
    }

    public void setFileNo(Long fileNo) {
        this.fileNo = fileNo;
    }

    public MultipartFile getImportAttacheFile() {
        return importAttacheFile;
    }

    public void setImportAttacheFile(MultipartFile importAttacheFile) {
        this.importAttacheFile = importAttacheFile;
    }

    public List<MultipartFile> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(List<MultipartFile> imageFiles) {
        this.imageFiles = imageFiles;
    }
}
