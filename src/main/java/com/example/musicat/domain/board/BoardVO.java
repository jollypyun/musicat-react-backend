package com.example.musicat.domain.board;

import java.util.ArrayList;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Alias("boardVO")
public class BoardVO {

    private int categoryNo;

    private int boardNo;
    private String boardName;
    private int boardkind; //게시판종류  / TINYINT / NotNull, 0, 1

    private int favoriateNo;
    private int no; //회원 넘버

    public BoardVO(int boardNo, String boardName) {
        super();
        this.boardNo = boardNo;
        this.boardName = boardName;
    }

    public BoardVO(int boardNo, String boardName, int boardkind) {
        this.boardNo = boardNo;
        this.boardName = boardName;
        this.boardkind = boardkind;
    }

}
