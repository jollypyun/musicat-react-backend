package com.example.musicat.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.musicat.domain.board.*;
import com.example.musicat.domain.member.MemberVO;
import com.example.musicat.service.board.*;
import com.example.musicat.util.FileManager;
import org.apache.commons.text.StringEscapeUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.example.musicat.service.board.ArticleService;
import com.example.musicat.service.board.BoardService;
import com.example.musicat.service.board.CategoryService;
import com.example.musicat.service.board.FileService;
import com.example.musicat.service.board.ReplyService;
import com.example.musicat.util.FileManager;
//import com.example.util.FileManager;
import com.example.musicat.domain.board.ArticleVO;
import com.example.musicat.domain.board.BoardBoardGradeVO;
import com.example.musicat.domain.board.BoardVO;
import com.example.musicat.domain.board.CategoryVO;
import com.example.musicat.domain.board.FileFormVO;
import com.example.musicat.domain.board.FileVO;
import com.example.musicat.domain.board.ReplyVO;
import com.example.musicat.domain.member.MemberVO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class ArticleController {

	private ArticleService articleService;
	private FileManager fileManager;
	private FileService fileService;
	private ReplyService replyService;
	private BoardService boardService;
	private CategoryService categoryService;

	@Autowired
	public ArticleController(ArticleService articleService, FileManager fileManager, FileService fileService,
			ReplyService replyService,  BoardService boardService, CategoryService categoryService) {
		this.articleService = articleService;
		this.fileManager = fileManager;
		this.fileService = fileService;
		this.replyService = replyService;
		this.boardService = boardService;
		this.categoryService = categoryService;
	}

	@GetMapping("/detailArticle/{articleNo}")
	public String detailArticle(@PathVariable("articleNo") int articleNo,HttpServletRequest request,HttpServletResponse response, Model model) {
		// create
		HttpSession session = request.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		ArticleVO article = this.articleService.retrieveArticle(articleNo);
		int boardNo = article.getBoardNo();
		int gradeNo = member.getGradeNo();
		boolean grade = this.boardService.retrieveAllReadBoard(boardNo, gradeNo);
		List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
		model.addAttribute("categoryBoardList", categoryList);
		CategoryVO categoryVo = new CategoryVO();
		model.addAttribute("categoryVo", categoryVo);
		
		// side bar -------------
		if (grade) {
			int memberNo = member.getNo();
			model.addAttribute("loginMemberNo", memberNo);
			this.articleService.upViewcount(articleNo); // 조회수 증가
			// bind
			List<ReplyVO> replys = this.replyService.retrieveAllReply(articleNo);
			int totalCount = this.articleService.totalRecCount(articleNo);
			int likeCheck = this.articleService.likeCheck(memberNo, articleNo);
			article.setLikeCheck(likeCheck);
			article.setNo(articleNo);
			article.setReplyList(replys);
			article.setLikecount(totalCount);
			// xss 처리 Html tag로 변환
			String escapeSubject = StringEscapeUtils.unescapeHtml4(article.getSubject());
			article.setSubject(escapeSubject);
			String escapeContent = StringEscapeUtils.unescapeHtml4(article.getContent());
			article.setContent(escapeContent);
			// view
			
			// side bar -------------
			model.addAttribute("article", article);
			log.info("detail 넘기는 aritcle: {}", article.toString());
			model.addAttribute("HomeContent", "/view/board/detailArticle");
		} else {
			model.addAttribute("HomeContent", "/view/board/accessDenied");
		}
		return "view/home/viewHomeTemplate";
	}

	@GetMapping("/writeArticleForm")
	public String writeForm(HttpServletRequest req, Model model) {
		// create
		ArticleVO articleVO = new ArticleVO(); // WriteForm에서 값들을 담을 객체
		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		// bind
		List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
		model.addAttribute("categoryBoardList", categoryList);
		CategoryVO categoryVo = new CategoryVO();
		model.addAttribute("categoryVo", categoryVo);
		
		int gradeNo = member.getGradeNo();
		log.info("writeForm get No::::" + gradeNo);
		// bind
		List<BoardVO> boardList = this.boardService.retrieveAllWriteBoard(gradeNo);
		// view
		model.addAttribute("boardList", boardList);
		model.addAttribute("articleVO", articleVO);
		model.addAttribute("gradeNo", member.getGradeNo()); // 나중에 seesion member에 접근해서 grade_no 받아올 것
		model.addAttribute("HomeContent", "/view/board/writeArticleForm");
		return "view/home/viewHomeTemplate";
	}

	@GetMapping("/updateArticleForm/{articleNo}")
	public String writeForm(@PathVariable int articleNo, HttpServletRequest req, Model model) {
//		log.info("updateForm articleNo: " + articleNo);
		ArticleVO article = this.articleService.retrieveArticle(articleNo); // 게시글 정보 가져오기
		// create
		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		int gradeNo = member.getGradeNo();
		List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
		model.addAttribute("categoryBoardList", categoryList);
		CategoryVO categoryVo = new CategoryVO();
		model.addAttribute("categoryVo", categoryVo);
		String escapeSubject = StringEscapeUtils.unescapeHtml4(article.getSubject());
		article.setSubject(escapeSubject);
		String escapeContent = StringEscapeUtils.unescapeHtml4(article.getContent());
		article.setContent(escapeContent);

		// bind
		List<BoardVO> boardList = this.boardService.retrieveAllWriteBoard(gradeNo);
		log.info("수정으로 넘어온 게시글 번호" +articleNo);
		// view
		model.addAttribute("boardList", boardList);
		model.addAttribute("article", article);
		model.addAttribute("gradeNo", member.getGrade()); // 나중에 seesion member에 접근해서 grade_no 받아올 것
		model.addAttribute("HomeContent", "/view/board/updateArticleForm");
		return "view/home/viewHomeTemplate";
	}

// Create

	@PostMapping("/insertArticle")
	public RedirectView insertArticle(@ModelAttribute("articleVO") ArticleVO articleVO, @ModelAttribute FileFormVO form,
			HttpServletRequest req) throws IOException {
		log.info("insert접근");
		// create
		RedirectView redirectView = new RedirectView();
		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		// 파일 첨부 지정 폴더에 Upload도 동시에 실행
		FileVO attacheFile = fileManager.uploadFile(form.getImportAttacheFile()); // 첨부 파일
		List<FileVO> imageFiles = fileManager.uploadFiles(form.getImageFiles()); // 이미지 파일
		if (imageFiles.size() > 0) {
			fileManager.createThumbnail(imageFiles.get(0).getSystemFileName()); // 썸네일 생성
		}
		// bind
		articleVO.setMemberNo(member.getNo());
		articleVO.setNickname(member.getNickname());
		articleVO.setAttacheFile(attacheFile);
		articleVO.setFileList(imageFiles);

		this.articleService.registerArticle(articleVO);
		int articleNo = articleVO.getNo(); // 작성 후 게시글 세부조회page로 넘어가기 때문에 게시글 번호를 넘겨준다.
		log.info("입력 게시글={}", articleVO.toString());
		// view
		redirectView.setUrl("/detailArticle/" + articleNo);
		return redirectView;
	}
	
	// 게시글 수정
	@PostMapping("/updateArticle/{articleNo}")
	public RedirectView updatetArticle(@ModelAttribute("article") ArticleVO articleVO,
			@ModelAttribute FileFormVO form,
			@PathVariable("articleNo") int articleNo,
			HttpServletRequest request)
			throws IOException {
		log.info("update접근");
		// create
		RedirectView redirectView = new RedirectView();
		// 파일 첨부 지정 폴더에 Upload도 동시에 실행
		FileVO attacheFile = fileManager.uploadFile(form.getImportAttacheFile()); // 첨부 파일
		List<FileVO> imageFiles = fileManager.uploadFiles(form.getImageFiles()); // 이미지 파일
		if (imageFiles.size() > 0) {
			fileManager.createThumbnail(imageFiles.get(0).getSystemFileName()); // 썸네일 생성
		}
		// bind
		articleVO.setNo(articleNo);
		articleVO.setAttacheFile(attacheFile);
		articleVO.setFileList(imageFiles);
		this.articleService.modifyArticle(articleVO);
//		int articleNo = articleVO.getNo();
		log.info("**********************: " + articleVO.toString());
		log.info("**********************: " + articleNo);
//		log.info("입력 게시글={}", articleVO.toString());
		// view
		redirectView.setUrl("/detailArticle/" + articleNo);
		return redirectView;
	}
	
	

// 게시판 목록 조회

	@GetMapping("/nListArticle/{boardNo}")
	public String selectAllNomalArticle(@PathVariable("boardNo") int boardNo,
														Model model) {
		// create
		List<ArticleVO> articles = this.articleService.retrieveBoard(boardNo);
		// bind
		FileVO file = new FileVO();
		for (ArticleVO article : articles) {
			// Html 변환
			String escapeSubject = StringEscapeUtils.unescapeHtml4(article.getSubject());
			article.setSubject(escapeSubject);
			
			file.setArticleNo(article.getNo());
			file.setFileType(1);
			FileVO thumbFile = this.fileService.retrieveThumbFile(file);
			if(thumbFile != null) {
				article.setThumbnail(thumbFile);
			} else {
				FileVO noFile = new FileVO();
				noFile.setSystemFileName("noimage.png");
				article.setThumbnail(noFile);
			}
		}
		List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
		CategoryVO categoryVo = new CategoryVO();
		
		BoardBoardGradeVO bbgVO = this.boardService.retrieveOneBoard(boardNo);
		String boardName = bbgVO.getBoardVo().getBoardName();
		
		
		int boardkind = bbgVO.getBoardVo().getBoardkind();
		
		model.addAttribute("categoryBoardList", categoryList);
		model.addAttribute("categoryVo", categoryVo);
		model.addAttribute("boardName", boardName); // 차후 이름으로 변경할것
		model.addAttribute("articles", articles); // 게시글 정보 전송
		model.addAttribute("boardkind", boardkind); // 게시글 유형
		return "/view/home/viewBoardTemplate";
	}
	
	
	
	@GetMapping("/removeArticle/{articleNo}/{boardNo}")
	public RedirectView removeArticle(@PathVariable("articleNo") int articleNo, HttpServletRequest req,
			@PathVariable("boardNo") int boardNo) {
		RedirectView redirectView = new RedirectView();
		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		int memberNo = member.getNo();
		this.articleService.removeArticle(articleNo, memberNo);
		redirectView.setUrl("/nListArticle/" + boardNo);
		return redirectView;
	}

	// 추천 
	@PostMapping("/addLike")
	@ResponseBody
	public Map<String, Object> recUpdate(@RequestBody HashMap<String, Object> map, HttpServletRequest request) {
		HttpSession session = request.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		int memberNo = member.getNo();
		int articleNo = Integer.parseInt((String)map.get("articleNo"));
//		log.info("******articleNO" + map.get("articleNo"));
		this.articleService.recUpdate(memberNo, articleNo);
		int totalCount = this.articleService.totalRecCount(articleNo);
		map.put("totalcount", totalCount);
		return map;
	}

	// 추천 취소
	@PostMapping("/delLike")
	@ResponseBody
	public Map<String, Object> recDelete(@RequestBody HashMap<String, Object> map, HttpServletRequest request) {
		HttpSession session = request.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		ArticleVO art = new ArticleVO();
		int articleNo = Integer.parseInt((String)map.get("articleNo"));
		art.setNo(articleNo);
		art.setMemberNo(member.getNo());
		this.articleService.recDelete(art);
		int totalCount = this.articleService.totalRecCount(articleNo);
		map.put("totalcount", totalCount);
		return map;
	}

}
