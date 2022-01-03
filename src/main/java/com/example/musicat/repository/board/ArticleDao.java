package com.example.musicat.repository.board;

import java.util.List;
import java.util.Map;

import com.example.musicat.domain.board.ArticleVO;
import com.example.musicat.domain.board.BestArticleVO;
import com.example.musicat.domain.board.SelectArticleVO;
import com.example.musicat.domain.board.TagVO;

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

	void insertTags(int articleNo, String[] tagList);

	List<TagVO> selectArticleTags(int articleNo);

	void deleteTag(int tagNo);

	List<ArticleVO> search(Map<String, Object> map);

	List<ArticleVO> selectBestArticle(); // 베스트글 조회

	void deleteAllBestArticle();

	void insertBestArticle(Map<String, Object> map);

	int selectNowDate(); // 현재 날짜 조회

	List<ArticleVO> selectUpdateBestArticle(int now); // 새로운 best글 뽑기

	boolean checkBestArticle(int articleNo);

	List<BestArticleVO> selectAllBestArticle();
}
