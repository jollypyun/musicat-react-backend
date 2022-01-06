package com.example.musicat.service.board;

import com.example.musicat.domain.board.ArticleVO;
import com.example.musicat.domain.board.BestArticleVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface ArticleService {

	//게시글 세부 조회
	ArticleVO retrieveArticle(int articleNo); 
	
	//게시판 별 게시글 목록 조회
	List<ArticleVO> retrieveBoard(int boardNo); 
	
	//게시글 작성
	void registerArticle(ArticleVO article);
	
	//게시글 수정
	void modifyArticle(ArticleVO article);
	
	//게시글 삭제
	int removeArticle(int articleNo,int memberNo);
	
	//전체글 조회
	List<ArticleVO> retrieveAllArticle();
	
	//조회수 증가
	void upViewcount(int articleNo);
	
	//추천
	void recUpdate(int articleNo, int memberNo);
	
	//총 추천 수
	int totalRecCount(int articleNo);
	
	//추천 취소
	void recDelete(ArticleVO aritcleVO);
	
	//추천 여부 체크
	int likeCheck(int memberNo, int ArticleNo);

	//태그 삭제
	void deleteTag(int tagNo);

	//게시글 검색
	List<ArticleVO> search(Map<String, Object> map);

	//베스트글 전체 조회
	List<BestArticleVO> selectAllBestArticle();
}
