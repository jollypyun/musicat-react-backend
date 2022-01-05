package com.example.musicat.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.example.musicat.controller.form.ArticleForm;
import com.example.musicat.domain.board.*;
import com.example.musicat.domain.member.MemberVO;
import com.example.musicat.repository.board.ArticleDao;
import com.example.musicat.util.FileManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.musicat.service.board.ArticleService;
import com.example.musicat.service.board.BoardService;
import com.example.musicat.service.board.CategoryService;
import com.example.musicat.service.board.FileService;
import com.example.musicat.service.board.ReplyService;
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
	 * @param req 사용자 정보 가져오기 위한 세션 접근
	 * @param model
	 * @return
	 */
	@GetMapping("/{articleNo}")
	public String detailArticle(@PathVariable("articleNo") int articleNo
			,HttpServletRequest req
			,Model model) {
		// create
		log.info("ArticleController.detailArticle: authAnon = " + SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());

		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");

		ArticleVO article = this.articleService.retrieveArticle(articleNo);
		log.info("Acontroller.detailArticle: -------" + article.toString());
		int boardNo = article.getBoardNo();
		int gradeNo = member.getGradeNo();
		boolean grade = this.boardService.retrieveAllReadBoard(boardNo, gradeNo);
		//boolean grade = true;
		List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
		model.addAttribute("categoryBoardList", categoryList);

		if (grade) {
			log.info("sidebar");
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

			model.addAttribute("article", result);
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
	public String writeForm(HttpServletRequest req
			,Model model) {
		// create
		ArticleForm form = new ArticleForm(); // 변경

		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		// bind
		List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
		model.addAttribute("categoryBoardList", categoryList);

		int gradeNo = member.getGradeNo();
		log.info("Start insertArticleCont--");
		log.info("writeForm get No::::" + gradeNo);
		// bind
		log.info("insert form 이동 권한 조회 전");
		List<BoardVO> boardList = this.boardService.retrieveAllWriteBoard(gradeNo);
		log.info("insert form 이동 권한 조회 후");
		log.info("End insertArticleCont--");
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
	public ModelAndView insertArticle(@Validated(ValidationSequence.class) @ModelAttribute("form") ArticleForm articleForm
			,BindingResult result
			,@ModelAttribute FileFormVO form
			,@RequestParam("tags") String tags
			,HttpServletRequest req) throws IOException {
		ModelAndView mv = new ModelAndView();
		log.info("insert접근");
		if (result.hasErrors()){
			List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
			mv.addObject("categoryBoardList", categoryList);
			List<BoardVO> boardList = this.boardService.retrieveAllWriteBoard(1);
			// view
			mv.addObject("boardList", boardList);
			mv.addObject("HomeContent", "/view/board/writeArticleForm");
			mv.setViewName("view/home/viewHomeTemplate");
			return mv;
		}
		// create
		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		// 파일 첨부 지정 폴더에 Upload도 동시에 실행
		FileVO attacheFile = fileManager.uploadFile(form.getImportAttacheFile()); // 첨부 파일
		List<FileVO> imageFiles = fileManager.uploadFiles(form.getImageFiles()); // 이미지 파일
		if (imageFiles.size() > 0) {
			fileManager.createThumbnail(imageFiles.get(0).getSystemFileName()); // 썸네일 생성
		}
		// bind
		ArticleVO article = ArticleVO.createArticle(member.getNo(), member.getNickname(), articleForm, attacheFile, imageFiles);

		if(!tags.equals("")){ //입력된 tag가 있는 경우
			String[] tagList = tags.split(","); // tag들
			article.setTagList(tagList);
		}

		this.articleService.registerArticle(article);
		int articleNo = article.getNo(); // 작성 후 게시글 세부조회page로 넘어가기 때문에 게시글 번호를 넘겨준다. (insert문 실행 뒤 Last ID 받아온다.)
		log.info("입력 게시글={}", article.toString());

		// view
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/articles/" + articleNo);
		mv.setView(redirectView);
		return mv;
	}

	/**
	 * 수정 폼 이동
	 */
	@GetMapping("/update/{articleNo}")
	public String updateForm(@PathVariable int articleNo
			,HttpServletRequest req
			,Model model) {
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
	public ModelAndView updatetArticle(@ModelAttribute("article") ArticleVO article
			,@Validated @ModelAttribute("form") ArticleForm articleForm
			,BindingResult result
			,@ModelAttribute FileFormVO form
			,@RequestParam("tags") String tags
			,@PathVariable("articleNo") int articleNo)
			throws IOException {
		log.info("update접근");

		ModelAndView mv = new ModelAndView();
		if (result.hasErrors()){
			List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
			mv.addObject("categoryBoardList", categoryList);
			List<BoardVO> boardList = this.boardService.retrieveAllWriteBoard(1);
			// view
			mv.addObject("boardList", boardList);
			mv.addObject("HomeContent", "/view/board/updateArticleForm");
			mv.setViewName("view/home/viewHomeTemplate");
			return mv;
		}

		// create
		if(!tags.equals("")){ //입력된 tag가 있는 경우
			log.info("tag null if문 입장");
			String[] tagList = tags.split(","); // tag들
			article.setTagList(tagList);
		}

		// 파일 첨부 지정 폴더에 Upload도 동시에 실행
		FileVO attacheFile = fileManager.uploadFile(form.getImportAttacheFile()); // 첨부 파일
		List<FileVO> imageFiles = fileManager.uploadFiles(form.getImageFiles()); // 이미지 파일
		if (imageFiles.size() > 0) {
			fileManager.createThumbnail(imageFiles.get(0).getSystemFileName()); // 썸네일 생성
		}
		// bind
		ArticleVO.updateArticle(article, articleNo, articleForm, attacheFile,imageFiles);
		this.articleService.modifyArticle(article);
		// view
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/articles/" + articleNo);
		mv.setView(redirectView);
		return mv;
	}

	@GetMapping("/remove/{articleNo}")
	public RedirectView removeArticle(@PathVariable("articleNo") int articleNo
			,HttpServletRequest req) {
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
	public Map<String, Object> recUpdate(@RequestBody HashMap<String, Object> map
			,HttpServletRequest req) {
		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		int memberNo = member.getNo();
		int articleNo = Integer.parseInt((String)map.get("articleNo"));

		this.articleService.recUpdate(memberNo, articleNo);
		int totalCount = this.articleService.totalRecCount(articleNo);
		map.put("totalcount", totalCount);
		return map;
	}

	// 추천 취소
	@PostMapping("/delLike")
	@ResponseBody
	public Map<String, Object> recDelete(@RequestBody HashMap<String, Object> map
			,HttpServletRequest req) {
		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		ArticleVO article = new ArticleVO();
		int articleNo = Integer.parseInt((String)map.get("articleNo"));
		article.setNo(articleNo);
		article.setMemberNo(member.getNo());
		this.articleService.recDelete(article);
		int totalCount = this.articleService.totalRecCount(articleNo);
		map.put("totalcount", totalCount);
		return map;
	}

	@PostMapping("/removeTag")
	@ResponseBody
	public List<TagVO> removeTag(@RequestParam("tagNo") int tagNo
			,@RequestParam("articleNo") int articleNo){
		articleService.deleteTag(tagNo);
		List<TagVO> findTags = articleDao.selectArticleTags(articleNo);
		return findTags;
	}

	/**
	 *  전체 검색
	 * view 연결만 하면 됨
	 * 값은 있음
	 */
	@GetMapping("/board/search")
	public String searchByBoard(@RequestParam("keyword") String keyword
			,@RequestParam("content") String content
			,Model model){

		HashMap<String, Object> searchMap = new HashMap<>();
		searchMap.put("keyword", keyword);
		searchMap.put("content", content);
		List<ArticleVO> articles = articleService.search(searchMap);
		model.addAttribute("articles", articles);

		List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
		model.addAttribute("categoryBoardList", categoryList);

		model.addAttribute("keyword",keyword); // 전체 검색 후 세부 검색을 위한 keyword
		model.addAttribute("content",content); // 전체 검색 후 세부 검색을 위한 content
		model.addAttribute("boardNo",0);
		model.addAttribute("boardName", "전체 검색");
		model.addAttribute("boardkind", 0);
		return "/view/home/viewBoardTemplate";
	}
}
