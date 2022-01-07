package com.example.musicat.service.board;

import com.example.musicat.domain.board.BoardBoardGradeVO;
import com.example.musicat.domain.board.BoardGradeVO;
import com.example.musicat.domain.board.BoardVO;

import java.util.ArrayList;
import java.util.List;


public interface BoardService {
	
	public void registerBoard(BoardVO boardVo, BoardGradeVO boardGradeVo);
	
	public ArrayList<BoardVO> retrieveBoardkind();
	
	public int retrieveConnectArticle(int boardNo);
	
	public void removeBoard(int boardNo);
	
	public void modifyBoard(BoardVO boardVo, BoardGradeVO boardGradeVo);
	
	public BoardBoardGradeVO retrieveOneBoard(int boardNo);
	
	public ArrayList<BoardBoardGradeVO> retrieveAllBoard();
	
	public List<BoardVO> retrieveAllWriteBoard(int gradeNo);
	
	public boolean retrieveAllReadBoard(int boardNo, int gradeNo);
	
	public Integer retrieveDuplicatedBoard(String boardName);
//	
//	public void registerFavoriteBoard(BoardVO boardVo);
//	
//	public void removeFavoriteBoard(BoardVO boardVo);
//	
//	public ArrayList<BoardVO> retrieveFavoriteBoardList(int no);

}
