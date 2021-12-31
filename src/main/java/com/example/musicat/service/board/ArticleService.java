package com.example.musicat.service.board;

import com.example.musicat.domain.board.ArticleVO;

import java.io.IOException;
import java.util.List;


public interface ArticleService {

	ArticleVO retrieveArticle(int articleNo); //세부 조회
	
	List<ArticleVO> retrieveBoard(int boardNo); // 목록 조회
	
	void registerArticle(ArticleVO article); // 추가
	
	void modifyArticle(ArticleVO article); // 수정
	
	void removeArticle(int articleNo,int memberNo); // 삭제
	
	List<ArticleVO> retrieveAllArticle(); // 전체글 조회
	
	void upViewcount(int articleNo); // 조회수 up
//	
	void recUpdate(int articleNo, int memberNo);
//	
	int totalRecCount(int articleNo);
	
	void recDelete(ArticleVO aritcleVO);
	
	int likeCheck(int memberNo, int ArticleNo);
}