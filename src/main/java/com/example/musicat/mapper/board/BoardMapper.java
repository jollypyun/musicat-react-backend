package com.example.musicat.mapper.board;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.musicat.domain.board.BoardBoardGradeVO;
import com.example.musicat.domain.board.BoardGradeVO;
import com.example.musicat.domain.board.BoardVO;
import com.example.musicat.domain.board.CategoryBoardVO;


@Mapper
public interface BoardMapper {
	
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

	//게시판이름 중복 검사
	public Integer selectDuplicatedBoard(String boardName);

	//게시판 이름 목록 조회
	//public List<BoardVO> selectBoardNameList();

	//즐겨찾기 게시판 추가
	public void insertLikeBoard(int memberNo, int boardNo);
	
	//즐겨찾기 게시판 삭제
	public void deleteLikeBoard(int memberNo, int boardNo);

	//즐겨찾기 게시판 여부 조회
	public int selectLikeBoard(int memberNo, int boardNo);
	
	//즐겨찾기 게시판 조회
	public ArrayList<BoardVO> selectLikeBoardList(int no);
	
	
	
	
	
}
