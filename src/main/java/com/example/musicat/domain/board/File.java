package com.example.musicat.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "files")
public class File {

    @Id @GeneratedValue
    @Column(name = "file_no")
    private Integer no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_no")
    private Article article;

    @Column(name = "originalfilename")
    private String originalFileName;

    @Column(name = "systemfilename")
    private String systemFileName;

    @Column(name = "filesize")
    private int fileSize;

    @Column(name = "filetype")
    private int fileType;

    @Column(name = "writedate", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp writedate;

    //==연관관계 편의 메소드==//
    public void addArticle(Article article) {
        this.article = article;
        article.getFileList().add(this);
    }

    //==생성 메소드==//
    public static File createFile(String originalFileName, String systemFileName, int fileSize, int fileType){
        File file = new File();
        file.originalFileName = originalFileName;
        file.systemFileName = systemFileName;
        file.fileSize = fileSize;
        file.fileType = fileType;
        return file;
    }

    @Override
    public String toString() {
        return "File{" +
                "no=" + no +
//                ", article=" + article +
                ", originalFileName='" + originalFileName + '\'' +
                ", systemFileName='" + systemFileName + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }

}
