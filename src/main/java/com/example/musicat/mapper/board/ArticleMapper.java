package com.example.musicat.mapper.board;

import java.util.List;
import java.util.Map;

import com.example.musicat.domain.board.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper {
	
	
	List<SelectArticleVO> selectArticle(int articleNo); //세부 조회
	
	List<ArticleVO> selectBoard(int boardNo); // 게시판 목록 조회
	
	void insertArticle(ArticleVO article); // 추가
	
	void updateArticle(ArticleVO article); // 수정
	
	void deleteArticle(int articleNo); // 삭제
	
	void upViewcount(int aritlceNo); // 조회수 증가
	
	List<ArticleVO> selectAllArticle();
	
	int likeCheck(ArticleVO articleVO); // 추천 햿는지 체크

	void insertLike(ArticleVO articleVO);
//	
	void deleteLike(ArticleVO articleVO); // 추천 삭제 
//	
	int totalLikeCount(int articleNo);
	
	void upLikecount(int articleNo);
	
	void downLikecount(int articleNo);

	void insertTag(TagVO tagVO);

	List<TagVO> selectArticleTags(int articleNo);

	void deleteTag(int tagNo);

	List<ArticleVO> search(Map<String, Object> map);

	List<ArticleVO> selectBestArticle();

	void deleteAllBestArticle(); // BestArticle 테이블 비우기

	void insertBestArticle(Map<String, Object> map);

//	List<> selectBest(); //Main Page 베스트글 출력 용

	int selectNowDate(); // 현재 날짜 조회

	List<ArticleVO> selectUpdateBestArticle(int now); // 새로운 best글 뽑기

	boolean checkBestArticle(int articleNo); // bestarticle table에 존재 여부 확인

	List<BestArticleVO> selectAllBestArticle(); // main page 출력 용
}
