package com.example.musicat.repository.board;

import com.example.musicat.domain.board.BoardBoardGradeVO;
import com.example.musicat.domain.board.BoardGradeVO;
import com.example.musicat.domain.board.BoardVO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public interface BoardDao {
	
	//게시판 추가
	public void insertBoard(BoardVO boardVo);
	
	public int selectLastInsertBoardNo();
	
	public void insertBoardGrade(BoardGradeVO boardGradeVo);
	
	//게시판 종류 조회
	public ArrayList<BoardVO> selectBoardkind();
	
	//연결된 게시글 조회
	public int selectConnectArticle(int boardNo);
	
	//게시판 삭제
	public void deleteBoard(int boardNo);
	
	//게시판 수정
	public void updateBoard(BoardVO boardVo, BoardGradeVO boardGradeVo);
	
	//게시판 정보 조회
	public BoardBoardGradeVO selectOneBoard(int boardNo);
	
	//게시판 정보 조회(전체)
	public ArrayList<BoardBoardGradeVO> selectAllBoard();
	
	//게시판 이름 중복 검사
	public int selectDuplicateBoard(String boardName);
	
	//즐겨찾기 게시판 추가
	public void insertFavoriteBoard(BoardVO boardVo);
	
	//즐겨찾기 게시판 삭제
	public void deleteFavoriteBoard(BoardVO boardVo);
	
	//즐겨찾기 게시판 조회
	public ArrayList<BoardVO> selectFavoriteBoardList(int no);
	
}
