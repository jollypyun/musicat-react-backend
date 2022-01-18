package com.example.musicat.domain.board;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Alias("boardVo")
public class BoardVO {

    private int categoryNo;

    private int boardNo;

    private String boardName;
    private int boardkind; //게시판종류  / TINYINT / NotNull, 0, 1
    private String boardkindName;


    private int favoriteNo;
    private int no; //회원 넘버

    //
//    public BoardVO(int categoryNo, int boardNo, String boardName, int boardkind) {
//        this.categoryNo = categoryNo;
//        this.boardNo = boardNo;
//        this.boardName = boardName;
//        this.boardkind = boardkind;
//    }
    //

    public BoardVO(int boardNo, String boardName, int boardkind) {
        this.boardNo = boardNo;
        this.boardName = boardName;
        this.boardkind = boardkind;
    }

    private List<BoardVO> boardGradeList = new ArrayList<>();


}
