package com.example.musicat.controller;

import java.util.*;

import com.example.musicat.domain.board.*;
import com.example.musicat.service.board.ArticleService;
import com.example.musicat.service.board.FileService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.musicat.service.board.BoardService;
import com.example.musicat.service.board.CategoryService;
import com.example.musicat.service.member.GradeService;
import com.example.musicat.domain.member.GradeVO;

import lombok.extern.slf4j.Slf4j;

import javax.naming.Binding;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Slf4j
@Controller
@Validated
public class BoardController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private GradeService gradeService;
	@Autowired ArticleService articleService;
	@Autowired FileService fileService;

//	@GetMapping("/main")
//	public String petopiaMain(Model model) {
//		model.addAttribute("HomeContent","fragments/categoryBoardListContent");
//		return "view/home/viewHomeTemplate";
//	}

	// 게시판
	@GetMapping("/board")
	public String petopiaMain(Model model) {
		model.addAttribute("HomeContent", "fragments/categoryBoardListContent");
		return "view/home/viewHomeTemplate";
	}

	//카테고리 + 게시판 목록 조회 ( 관리자 )
	@GetMapping("/boardManager")
	public String boardManager(Model model) {
		List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
		model.addAttribute("categoryBoardList", categoryList);
		model.addAttribute("managerContent", "/view/board/boardManager");
		
		log.info("여기에요!!!-------------------" + categoryList.toString());
		
		//카테고리 추가
		CategoryVO categoryVo = new CategoryVO();
		model.addAttribute("categoryVo", categoryVo);
		return "view/home/viewManagerTemplate";
	}
	
	
	
	//카테고리 추가
	@PostMapping("/writeCategory")
	public String writeCategory(@ModelAttribute("categoryVo") CategoryVO categoryVo) {
		this.categoryService.registerCategory(categoryVo.getCategoryName());
		return "redirect:boardManager"; //이  페이지 안에서만 움직일 거기 때문에 리턴 페이지 변경 안 해도 된다고
	}

	//카테고리 수정 페이지
	@ResponseBody
	@PostMapping("/selectOneCategory")
	public CategoryVO selectOneCategory(@RequestBody HashMap<String, Object> map) throws Exception {

		CategoryVO cVO = new CategoryVO();

		int categoryNo = Integer.parseInt((String) map.get("categoryNo"));
		String categoryName = this.categoryService.retrieveOneCategory(categoryNo).getCategoryName();

		cVO.setCategoryName(categoryName);
		return cVO;
	}

	//카테고리 수정
	@PostMapping("/modifyCategory")
	public String modifyCategory(@ModelAttribute("categoryVo") CategoryVO categoryVo) {
		int categoryNo = categoryVo.getCategoryNo();
		String categoryName = categoryVo.getCategoryName();
		this.categoryService.modifyCategory(categoryNo, categoryName);
		return "redirect:boardManager";
	}

	
	//카테고리 삭제
	@PostMapping("/deleteCategory")
	public String deleteCategory(@ModelAttribute("categoryVo") CategoryVO categoryVo) {
		log.info("----------------------------------" + categoryVo.getCategoryNo());
		this.categoryService.removeCategory(categoryVo.getCategoryNo());
		return "redirect:boardManager";
	}
	

	//게시판 추가 페이지 드롭박스 목록
	@ResponseBody
	@PostMapping("/selectListAdd")
	public CreateBoardVO selectListAdd() throws Exception {
		CreateBoardVO cbVO = new CreateBoardVO();
		//카테고리 목록
		ArrayList<CategoryVO> categoryList = this.categoryService.retrieveCategoryList();
		//등급 목록
		ArrayList<GradeVO> gradeList = this.gradeService.retrieveGradeList();
		//게시판 종류 목록
		ArrayList<BoardVO> boardkindList = this.boardService.retrieveBoardkind();

		cbVO.setCategoryList(categoryList);
		cbVO.setGradeList(gradeList);
		cbVO.setBoardkindList(boardkindList);

		log.info("/selectListAdd");

		return cbVO;
	}
	
	
	//게시판 추가 - 저장
//	@PostMapping("/writeBoard")
//	public String writeBoard(
//			@RequestParam("category") int categoryNo,
//			@RequestParam("boardName") @Size(min=1, max = 1) String boardName,
//			@RequestParam("boardkind") int boardkind,
//			@RequestParam("readGrade") int readGrade,
//			@RequestParam("writeGrade") int writeGrade) {
//
//
//		BoardVO boardVo = new BoardVO();
//		BoardGradeVO boardGradeVo = new BoardGradeVO();
//		//게시판 저장 목록
//		boardVo.setCategoryNo(categoryNo);
//		boardVo.setBoardName(boardName);
//		boardVo.setBoardkind(boardkind);
//		boardGradeVo.setReadGrade(readGrade);
//		boardGradeVo.setWriteGrade(writeGrade);
//		this.boardService.registerBoard(boardVo, boardGradeVo);
//		return "redirect:boardManager";
//	}

	@PostMapping("/writeBoard")
	public String writeBoard(
			@RequestParam("category") int categoryNo,
			@RequestParam("boardName") @Size(min=1, max = 1) String boardName,
			@RequestParam("boardkind") int boardkind,
			@RequestParam("readGrade") int readGrade,
			@RequestParam("writeGrade") int writeGrade) {


		BoardVO boardVo = new BoardVO();
		BoardGradeVO boardGradeVo = new BoardGradeVO();
		//게시판 저장 목록
		boardVo.setCategoryNo(categoryNo);
		boardVo.setBoardName(boardName);
		boardVo.setBoardkind(boardkind);
		boardGradeVo.setReadGrade(readGrade);
		boardGradeVo.setWriteGrade(writeGrade);
		this.boardService.registerBoard(boardVo, boardGradeVo);
		return "redirect:boardManager";
	}

	//게시판 수정 페이지
	@ResponseBody
	@PostMapping("/selectListModify")
	public CreateBoardVO selectListModify(@RequestBody HashMap<String, Object> map ) throws Exception {
		CreateBoardVO cbVO = new CreateBoardVO();
		//카테고리 목록
		ArrayList<CategoryVO> categoryList = this.categoryService.retrieveCategoryList();
		//등급 목록
		ArrayList<GradeVO> gradeList = this.gradeService.retrieveGradeList();
		//게시판 종류 목록
		ArrayList<BoardVO> boardkindList = this.boardService.retrieveBoardkind();

		//해당 게시판의 정보
		int boardNo = Integer.parseInt((String) map.get("boardNo"));
		BoardBoardGradeVO bbg = this.boardService.retrieveOneBoard(boardNo);

		cbVO.setBbg(bbg);
		cbVO.setCategoryList(categoryList);
		cbVO.setGradeList(gradeList);
		cbVO.setBoardkindList(boardkindList);

		return cbVO;
	}

	// 게시판 수정
	@PostMapping("/modifyBoard")
	public String modifyBoard(
			@RequestParam("boardNo") int boardNo,
			@RequestParam("category") int categoryNo,
			@RequestParam("boardName") String boardName,
			@RequestParam("boardkind") int boardkind,
			@RequestParam("readGrade") int readGrade,
			@RequestParam("writeGrade") int writeGrade) {
		
		BoardVO boardVo = new BoardVO();
		BoardGradeVO boardGradeVo = new BoardGradeVO();

		//게시판 저장 목록
		boardVo.setBoardNo(boardNo);
		boardVo.setCategoryNo(categoryNo);
		boardVo.setBoardName(boardName);
		boardVo.setBoardkind(boardkind);
		boardGradeVo.setReadGrade(readGrade);
		boardGradeVo.setWriteGrade(writeGrade);

		this.boardService.modifyBoard(boardVo, boardGradeVo);
		return "redirect:boardManager";
	}


	
	//게시판 삭제
	@PostMapping("/deleteBoard")
	public String deleteBoard(@ModelAttribute("boardVo") BoardVO boardVo) {
		log.info("deleteBoard-------------------------" + boardVo.getBoardNo());
		this.boardService.removeBoard(boardVo.getBoardNo());
		return "redirect:boardManager";
	} 

			
			
	
//	@GetMapping("/nListArticlereq/{boardNo}/{boardkind}")
//	public RedirectView amuguna(@PathVariable("boardNo") int boardNo, @PathVariable("boardkind") int boardkind, HttpServletRequest request) {
//		RedirectView rv = new RedirectView();
//		request.setAttribute("boardNo", boardNo);
//		rv.addStaticAttribute("boardKind", boardkind);
//		System.out.println("BoardController boardkind --------------------- " + boardkind);
//		System.out.println("BoardController boardNo --------------------- " + boardNo);
//		rv.setUrl("/nListArticle/" + boardNo);
//		return rv;
//	}


	// 게시판 목록 조회
	@GetMapping("/board/{boardNo}/articles")
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

		model.addAttribute("boardNo", boardNo);
		model.addAttribute("categoryBoardList", categoryList);
		model.addAttribute("boardName", boardName); // 차후 이름으로 변경할것
		model.addAttribute("articles", articles); // 게시글 정보 전송
		model.addAttribute("boardkind", boardkind); // 게시글 유형
		return "/view/home/viewBoardTemplate";
	}

	// 게시판 내 검색
	@GetMapping("/board/search")
	@ResponseBody
	public List<ArticleVO> searchByBoard(@RequestParam("keyword") String keyword
			,@RequestParam("content") String content
			,@RequestParam("boardNo") Integer boardNo
			,@RequestParam("aKeyword") String aKeyword
			,@RequestParam("aContent") String aContent){
		HashMap<String, Object> searchMap = new HashMap<>();
		if(aKeyword != null){
			if(!aKeyword.equals(keyword)){ // keyword가 같다면 생략
				searchMap.put(aKeyword, aContent);
			}
		}
		if(boardNo > 0){
			searchMap.put("boardNo", boardNo);
		}
		searchMap.put("keyword", keyword);
		searchMap.put("content", content);

		List<ArticleVO> results = articleService.search(searchMap);
		return results;
	}
	
}
