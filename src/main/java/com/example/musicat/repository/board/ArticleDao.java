package com.example.musicat.repository.board;

import java.util.List;

import com.example.musicat.domain.board.ArticleVO;
import com.example.musicat.domain.board.SelectArticleVO;

public interface ArticleDao {

	List<SelectArticleVO> selectArticle(int articleNo); //세부 조회
	
	List<ArticleVO> selectBoard(int boardNo); // 목록 조회
	
	void insertArticle(ArticleVO article); // 추가
	
	void updateArticle(ArticleVO article); // 수정
	
	void deleteArticle(int articleNo); // 삭제
	
	void upViewcount(int articleNo); // 조회수 up
	
	List<ArticleVO> selectAllArticle();

	int likeCheck(ArticleVO articleVO);
	
	void insertLike(ArticleVO articleVO);
//	
	void deleteLike(ArticleVO articleVO);  // 추천 삭제
//	
	int totalRecCount(int totalRecCount);
	
	void upLikecount(int articleNo);
	
	void downLikecount(int articleNo);
}
