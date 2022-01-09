package com.example.musicat.service.board;

import java.util.ArrayList;
import java.util.List;

import com.example.musicat.domain.board.BoardBoardGradeVO;
import com.example.musicat.domain.board.BoardGradeVO;
import com.example.musicat.domain.board.BoardVO;
import com.example.musicat.repository.board.BoardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("boardService")
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDao boardDao;

	// 게시판 추가(기본정보 + 등급)
	@Override
	@Transactional
	public void registerBoard(BoardVO boardVo, BoardGradeVO boardGradeVo) {
		this.boardDao.insertBoard(boardVo);
		boardGradeVo.setBoardNo(this.boardDao.selectLastInsertBoardNo());
		this.boardDao.insertBoardGrade(boardGradeVo);
	}

	// 게시판 종류 조회
	@Override
	public ArrayList<BoardVO> retrieveBoardkind() {
		ArrayList<BoardVO> boardkindList = this.boardDao.selectBoardkind();
		return boardkindList;
	}

	// 연결된 게시글 조회
	@Override
	public int retrieveConnectArticle(int boardNo) {
		int connectArticle = this.boardDao.selectConnectArticle(boardNo);
		return connectArticle;
	}

	// 게시판 삭제
	@Override
	public void removeBoard(int boardNo) {
		this.boardDao.deleteBoard(boardNo);

	}

	// 게시판 수정
	@Override
	public void modifyBoard(BoardVO boardVo, BoardGradeVO boardGradeVo) {
		this.boardDao.updateBoard(boardVo, boardGradeVo);
		log.info("-------------Modiservice" + boardVo.toString());
		log.info("-------------Modiservice" + boardGradeVo.toString());
	}

	// 게시판 정보 조회
	@Override
	public BoardBoardGradeVO retrieveOneBoard(int boardNo) {
		BoardBoardGradeVO boardGradeVo = this.boardDao.selectOneBoard(boardNo);
		return boardGradeVo;
	}

	// 게시판 정보 조회 (전체)
	@Override
	public ArrayList<BoardBoardGradeVO> retrieveAllBoard() {
		ArrayList<BoardBoardGradeVO> boardAndGradeList = this.boardDao.selectAllBoard();
		return boardAndGradeList;
	}

	//게시판 이름 중복 여부 조회
	@Override
	public Integer retrieveDuplicatedBoard(String boardName) {
		Integer duplicatedBoard = this.boardDao.selectDuplicatedBoard(boardName);
		return duplicatedBoard;
	}

	@Override
	public List<BoardVO> retrieveAllWriteBoard(int gradeNo) {
		ArrayList<BoardBoardGradeVO> boardAndGradeList = this.boardDao.selectAllBoard();
		List<BoardBoardGradeVO> writeBoardList = new ArrayList<>();
		List<BoardVO> resultBoardList = new ArrayList<>();
		for (BoardBoardGradeVO bbg : boardAndGradeList) {
			// 쓰기 등급 List 가져오기
			if (bbg.getBoardGradeVo().getReadwrite() == 0) {
				writeBoardList.add(bbg);
//				log.info("wrteList: " + bbg.toString());
			}
		}
		for (BoardBoardGradeVO write : writeBoardList) {
			if (write.getBoardGradeVo().getGradeNo() >= gradeNo) {
				resultBoardList.add(write.getBoardVo());
//				log.info("**Result**" + write.toString());
			}
		}
		return resultBoardList;
	}

	@Override
	public boolean retrieveAllReadBoard(int boardNo, int gradeNo) {
		BoardBoardGradeVO bbgVO = this.boardDao.selectOneBoard(boardNo);
		if (bbgVO.getBoardGradeVo().getReadGrade() >= gradeNo) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public List<BoardVO> retrieveBoardNameList() {
		return this.boardDao.selectBoardNameList();
	}

	//
//	//즐겨찾기 게시판 추가
//	@Override
//	public void registerFavoriteBoard(BoardVO boardVo) {
//		this.boardDao.insertFavoriteBoard(boardVo);
//	}
//
//	//즐겨찾기 게시판 삭제
//	@Override
//	public void removeFavoriteBoard(BoardVO boardVo) {
//		this.boardDao.deleteFavoriteBoard(boardVo);
//	}
//
//	//즐겨찾기 게시판 조회
//	@Override
//	public ArrayList<BoardVO> retrieveFavoriteBoardList(int no) {
//		ArrayList<BoardVO> favoriteList = this.boardDao.selectFavoriteBoardList(no);
//		return favoriteList;
//	}
}
