package com.example.musicat.domain.board;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
@Alias("categoryVo")
@ToString
public class CategoryVO {
    private int categoryNo;

    //@Size(min=1, max = 20, message = "카테고리 이름은 공백일 수 없습니다. (1 ~ 20자)")
    private String categoryName;

    private List<CategoryVO> categoryList = new ArrayList<>();
    private List<BoardVO> boardList = new ArrayList<>();

    public void addBoardList(BoardVO board) {
        this.boardList.add(board);
    }


    //    public void addCategoryList(CategoryVO categoryVo) {
//        this.categoryList.add(categoryVo);
//    }
}
