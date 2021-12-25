package com.example.musicat.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "files")
public class File {

    @Id @GeneratedValue
    @Column(name = "file_no")
    Integer no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_no")
    Article article;

    @Column(name = "originalfilename")
    String originalFileName;

    @Column(name = "systemfilename")
    String systemFileName;

    @Column(name = "filesize")
    int fileSize;

    //==연관관계 편의 메소드==//
    public void addArticle(Article article) {
        this.article = article;
        article.getFileList().add(this);
    }

}
