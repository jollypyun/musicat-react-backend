package com.example.musicat.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.musicat.controller.form.ArticleForm;
import com.example.musicat.domain.board.*;
import com.example.musicat.domain.member.MemberVO;
import com.example.musicat.repository.board.ArticleDao;
import com.example.musicat.util.FileManager;
import org.apache.commons.text.StringEscapeUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.musicat.service.board.ArticleService;
import com.example.musicat.service.board.BoardService;
import com.example.musicat.service.board.CategoryService;
import com.example.musicat.service.board.FileService;
import com.example.musicat.service.board.ReplyService;
//import com.example.util.FileManager;
import com.example.musicat.controller.form.FileFormVO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("articles")
public class ArticleController {

	private ArticleService articleService;
	private FileManager fileManager;
	private FileService fileService;
	private ReplyService replyService;
	private BoardService boardService;
	private CategoryService categoryService;
	private ArticleDao articleDao;

	@Autowired
	public ArticleController(ArticleService articleService, FileManager fileManager, FileService fileService, ReplyService replyService, BoardService boardService, CategoryService categoryService, ArticleDao articleDao) {
		this.articleService = articleService;
		this.fileManager = fileManager;
		this.fileService = fileService;
		this.replyService = replyService;
		this.boardService = boardService;
		this.categoryService = categoryService;
		this.articleDao = articleDao;
	}

	/**
	 * 세부 조회
	 * @param articleNo 게시글 번호
	 * @param request 사용자 정보 가져오기 위한 세션 접근
	 * @param response
	 * @param model
	 * @return
	 */
	@GetMapping("/{articleNo}")
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

		// side bar -------------
		if (grade) {
			int memberNo = member.getNo();
			model.addAttribute("loginMemberNo", memberNo);
			this.articleService.upViewcount(articleNo); // 조회수 증가
			// bind
			List<ReplyVO> replys = this.replyService.retrieveAllReply(articleNo);
			int totalCount = this.articleService.totalRecCount(articleNo);
			int likeCheck = this.articleService.likeCheck(memberNo, articleNo);

			ArticleVO result = ArticleVO.addReplyAndLike(article, likeCheck, replys, totalCount); //리팩토링

			// xss 처리 Html tag로 변환
//			String escapeSubject = StringEscapeUtils.unescapeHtml4(article.getSubject());
//			article.setSubject(escapeSubject);
//			String escapeContent = StringEscapeUtils.unescapeHtml4(article.getContent());
//			article.setContent(escapeContent);
			// view
			
			// side bar -------------
			model.addAttribute("article", result);
			log.info("detail 넘기는 aritcle: {}", article.toString());
			model.addAttribute("HomeContent", "/view/board/detailArticle");
		} else {
			model.addAttribute("HomeContent", "/view/board/accessDenied");
		}
		return "view/home/viewHomeTemplate";
	}

	/**
	 * 작성 폼 이동
	 */
	@GetMapping("/insert")
	public String writeForm(HttpServletRequest req, Model model) {
		// create
//		ArticleVO articleVO = new ArticleVO(); // WriteForm에서 값들을 담을 객체
		ArticleForm form = new ArticleForm(); // 변경

		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		// bind
		List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
		model.addAttribute("categoryBoardList", categoryList);

		int gradeNo = member.getGradeNo();
		log.info("writeForm get No::::" + gradeNo);
		// bind
		List<BoardVO> boardList = this.boardService.retrieveAllWriteBoard(gradeNo);
		// view
		model.addAttribute("boardList", boardList);
		model.addAttribute("form", form);
		model.addAttribute("gradeNo", gradeNo); // 나중에 seesion member에 접근해서 grade_no 받아올 것
		model.addAttribute("HomeContent", "/view/board/writeArticleForm");
		return "view/home/viewHomeTemplate";
	}

	/**
	 * 작성
	 */
	@PostMapping("/insert")
	public RedirectView insertArticle(@ModelAttribute("form") ArticleForm articleForm, @ModelAttribute FileFormVO form,
									  @RequestParam("tags") String tags , HttpServletRequest req) throws IOException {
		log.info("insert접근");
		// create
		String[] tagList = tags.split(","); // tag들
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
		ArticleVO article = ArticleVO.createArticle(member.getNo(), member.getNickname(), articleForm, attacheFile, imageFiles, tagList);

		this.articleService.registerArticle(article);
		int articleNo = article.getNo(); // 작성 후 게시글 세부조회page로 넘어가기 때문에 게시글 번호를 넘겨준다. (insert문 실행 뒤 Last ID 받아온다.)
		log.info("입력 게시글={}", article.toString());
		// view
		redirectView.setUrl("/articles/" + articleNo);
		return redirectView;
	}


	/**
	 * 수정 폼 이동
	 */
	@GetMapping("/update/{articleNo}")
	public String updateForm(@PathVariable int articleNo, HttpServletRequest req, Model model) {
//		log.info("updateForm articleNo: " + articleNo);
		ArticleVO article = this.articleService.retrieveArticle(articleNo); // 게시글 정보 가져오기
		ArticleForm form = ArticleForm.updateArticle(article);
		// create
		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		int gradeNo = member.getGradeNo();

		List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
		model.addAttribute("categoryBoardList", categoryList);

		// bind
		List<BoardVO> boardList = this.boardService.retrieveAllWriteBoard(gradeNo);
		log.info("수정으로 넘어온 게시글 번호" +articleNo);

		// view
		model.addAttribute("boardList", boardList);
		model.addAttribute("form", form); //유효성 검사 항목들
		model.addAttribute("article", article);
		model.addAttribute("gradeNo", gradeNo); // 나중에 seesion member에 접근해서 grade_no 받아올 것
		model.addAttribute("HomeContent", "/view/board/updateArticleForm");
		return "view/home/viewHomeTemplate";
	}

	// 게시글 수정
	@PostMapping("/update/{articleNo}")
	public RedirectView updatetArticle(@ModelAttribute("article") ArticleVO article
			,@ModelAttribute("form") ArticleForm articleForm
			,@ModelAttribute FileFormVO form, @RequestParam("tags") String tags,
			@PathVariable("articleNo") int articleNo,
			HttpServletRequest request)
			throws IOException {
		log.info("update접근");
		// create
		String[] tagList = tags.split(",");
		for (String s : tagList) {
			log.info("==========");
			log.info("S: {}",s);
			log.info("==========");
		}
		article.setTagList(tagList);
		RedirectView redirectView = new RedirectView();
		// 파일 첨부 지정 폴더에 Upload도 동시에 실행
		FileVO attacheFile = fileManager.uploadFile(form.getImportAttacheFile()); // 첨부 파일
		List<FileVO> imageFiles = fileManager.uploadFiles(form.getImageFiles()); // 이미지 파일
		if (imageFiles.size() > 0) {
			fileManager.createThumbnail(imageFiles.get(0).getSystemFileName()); // 썸네일 생성
		}
		// bind
		ArticleVO.updateArticle(article, articleNo, articleForm,attacheFile,imageFiles);
		this.articleService.modifyArticle(article);
//		int articleNo = article.getNo();
		log.info("**********************: " + article.toString());
		log.info("**********************: " + articleNo);
		// view
		redirectView.setUrl("/articles/" + articleNo);
		return redirectView;
	}
	


	@GetMapping("/remove/{articleNo}")
	public RedirectView removeArticle(@PathVariable("articleNo") int articleNo, HttpServletRequest req) {
		RedirectView redirectView = new RedirectView();
		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		int memberNo = member.getNo();
		int boardNo = this.articleService.removeArticle(articleNo, memberNo);
		redirectView.setUrl("/board/" + boardNo + "/articles");
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


	@PostMapping("/removeTag")
	@ResponseBody
	public List<TagVO> removeTag(@RequestParam("tagNo") int tagNo, @RequestParam("articleNo") int articleNo){
		articleService.deleteTag(tagNo);
		List<TagVO> findTags = articleDao.selectArticleTags(articleNo);
		return findTags;
	}

	// 전체 검색
	@GetMapping("/board/search")
	public List<ArticleVO> searchByBoard(@RequestParam("keyword") String keyword
			, @RequestParam("content") String content, Model model){
		HashMap<String, String> searchMap = new HashMap<>();
		searchMap.put("keyword", keyword);
		searchMap.put("content", content);
		List<ArticleVO> results = articleService.search(searchMap);
		model.addAttribute("articles", results);
		return results;
	}



}
