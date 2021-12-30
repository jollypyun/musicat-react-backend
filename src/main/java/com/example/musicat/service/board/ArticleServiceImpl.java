package com.example.musicat.service.board;

import java.util.ArrayList;
import java.util.List;

import com.example.musicat.domain.board.ArticleVO;
import com.example.musicat.domain.board.FileVO;
import com.example.musicat.domain.board.SelectArticleVO;
import com.example.musicat.mapper.member.MemberMapper;
import com.example.musicat.repository.board.ArticleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private FileService fileService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private MemberMapper memberMapper;

	@Override
	@Transactional
	public ArticleVO retrieveArticle(int articleNo) { // 세부 조회
		List<SelectArticleVO> results = this.articleDao.selectArticle(articleNo);
		System.out.println("results : " + results.size());
		ArticleVO article = results.get(0).getArticle(); // 게시글 정보 출력
		List<FileVO> fileList = new ArrayList<>();
			if(results.get(0).getFile() != null) { // 파일이 있을 때
				for (SelectArticleVO result : results) {
					fileList.add(result.getFile());
				}
			} else { // 첨부된 파일이 없을 때
				fileList=null;
			}
		return this.fileService.fileList(article, fileList);
	}

	@Override
	public List<ArticleVO> retrieveBoard(int boardNo) { // 게시판 목록 조회
		return this.articleDao.selectBoard(boardNo);
	}

	// 게시글 추가
	@Override
	@Transactional
	public void registerArticle(ArticleVO article) {
		this.memberMapper.plusMemberDocs(article.getMemberNo());
		this.articleDao.insertArticle(article); // 게시글 추가
		this.fileService.uploadFile(article);
	}

	@Override
	@Transactional
	public void modifyArticle(ArticleVO article) {
		this.articleDao.updateArticle(article);
		this.fileService.uploadFile(article);
	}

	@Override
	@Transactional
	public int removeArticle(int articleNo, int memberNo) {
		this.memberMapper.minusMemberDocs(memberNo);
		this.fileService.allDelete(articleNo);
		this.replyService.allDelete(articleNo);
		int boardNo = this.articleDao.selectArticle(articleNo).get(0).getArticle().getBoardNo();
		this.articleDao.deleteArticle(articleNo);
		return boardNo;
	}

	@Override
	public void upViewcount(int articleNo) {
		this.articleDao.upViewcount(articleNo);
	}
	
	@Override
	public List<ArticleVO> retrieveAllArticle() {
		return this.articleDao.selectAllArticle();
	}
	
	
	// 추천
	@Override
	public void recUpdate(int memberNo, int articleNo) {
		ArticleVO articleVO = new ArticleVO();
		articleVO.setMemberNo(memberNo);
		articleVO.setNo(articleNo);
		this.articleDao.upLikecount(articleNo);
		this.articleDao.insertLike(articleVO);	
	}
//	
	@Override
	public int totalRecCount(int articleNo) {
		return this.articleDao.totalRecCount(articleNo);
	}
	
	@Override
	public void recDelete(ArticleVO articleVO) {
		this.articleDao.downLikecount(articleVO.getNo());
		this.articleDao.deleteLike(articleVO);
	}

	// 추천 여부 체크
	@Override
	public int likeCheck(int memberNo, int ArticleNo) {
		ArticleVO art = new ArticleVO();
		art.setMemberNo(memberNo);
		art.setNo(ArticleNo);
		return this.articleDao.likeCheck(art);
	}
	
}
