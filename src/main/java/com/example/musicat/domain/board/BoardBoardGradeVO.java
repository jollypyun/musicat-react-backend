package com.example.musicat.domain.board;

import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Repository;

import lombok.Data;

@Data
@Alias("boardBoardGradeVo")
public class BoardBoardGradeVO {
	private BoardVO boardVo;
	private BoardGradeVO boardGradeVo;
}
