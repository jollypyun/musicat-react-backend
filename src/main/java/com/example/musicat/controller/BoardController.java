package com.example.musicat.controller;

import java.util.*;

import com.example.musicat.domain.board.*;
import com.example.musicat.domain.member.MemberVO;
import com.example.musicat.service.board.ArticleService;
import com.example.musicat.service.board.FileService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import javax.validation.Valid;

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
	@Autowired
	ArticleService articleService;

	@Autowired
	FileService fileService;

	// 메인화면 사이드바
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

		log.info("boardManager--------" + categoryList.toString());
		
		//카테고리 추가
		CategoryVO categoryVo = new CategoryVO();
		model.addAttribute("categoryVo", categoryVo);
		return "view/home/viewManagerTemplate";
	}

	@GetMapping("/accessDenideGrade")
	public String accessDenied(Model model) {
		log.info("/accessDenideGrade------------------------------------");
		model.addAttribute("managerContent", "view/security/accessDenideGrade");
		return "view/home/viewManagerTemplate";
	}

	//카테고리 추가
	@ResponseBody
	@PostMapping("/writeCategory")
	public Map<String, Integer> writeCategory(@ModelAttribute("categoryVo") CategoryVO categoryVo) {

		Map<String, Integer> map = new HashMap<>();

		String categoryName = categoryVo.getCategoryName();

		Integer duplicatedCategory = categoryService.retrieveDuplicatedCategory(categoryName);

		if (duplicatedCategory == null) {
			map.put("result", 0);
			this.categoryService.registerCategory(categoryVo.getCategoryName());
		} else {
			map.put("result", 1);
		}
		return map;
	}


	//카테고리 수정 페이지
	@ResponseBody
	@PostMapping("/selectOneCategory")
	public CategoryVO selectOneCategory(@RequestBody HashMap<String, Object> map) throws Exception {

		CategoryVO cVO = new CategoryVO();

		int categoryNo = Integer.parseInt((String) map.get("categoryNo"));
		String categoryName = this.categoryService.retrieveOneCategory(categoryNo).getCategoryName();

		cVO.setCategoryNo(categoryNo);
		cVO.setCategoryName(categoryName);
		return cVO;
	}


	// 카테고리 수정
	@ResponseBody
	@PostMapping("/modifyCategory")
	public Map<String, Integer> modifyCategory ( @Valid @ModelAttribute("categoryVo") CategoryVO categoryVo) {
		Map<String, Integer> map = new HashMap<>();

		int categoryNo = categoryVo.getCategoryNo();
		String categoryName = categoryVo.getCategoryName();

		//카테고리명 중복 검사
		Integer duplicatedCategory = this.categoryService.retrieveDuplicatedCategory(categoryName);
		if (duplicatedCategory == null) { //중복 x
			map.put("result", 0);
			this.categoryService.modifyCategory(categoryNo, categoryName);
		} else { //중복 o
			if(duplicatedCategory == categoryNo) {
				map.put("result", 0); //해당 카테고리면 저장o
			} else {
				map.put("result", 1); //다른 카테고리면 저장x
			}
		}

		return map;
//
//
//		if (count != 0) {
//			//log.info("수정 불가");
//			//int duplicatedCategory = 1;
//			map.put("result", 1);
//		} else {
//			//log.info("수정 가능");
//			//int duplicatedCategory = 0;
//			map.put("result", 0);
//			this.categoryService.modifyCategory(categoryNo, categoryName);
//			//this.categoryService.registerCategory(categoryVo.getCategoryName());
//		}
//		log.info("/modifyCategory-----------------------");
//		return map;
	}


	//카테고리 삭제
	@ResponseBody
	@PostMapping("/deleteCategory")
	public Map<String, Integer> deleteCategory(@ModelAttribute("categoryVo") CategoryVO categoryVo) {

		int categoryNo = categoryVo.getCategoryNo();
		int count = categoryService.retrieveConnectBoard(categoryNo);
		log.info("count count count {}", count);

		Map<String, Integer> map = new HashMap<>();
		if (count != 0) {
			map.put("result", 1);
		} else {
			map.put("result", 0);

			this.categoryService.removeCategory(categoryNo);
		}
		return map;
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
	@ResponseBody
	@PostMapping("/writeBoard")
	public Map<String, Integer> writeBoard(
			@RequestParam("categoryNo") int categoryNo,
			@RequestParam("boardName") String boardName,
			@RequestParam("writeGrade") int writeGrade,
			@RequestParam("readGrade") int readGrade,
			@RequestParam("boardkind") int boardkind) {

		Map<String, Integer> map = new HashMap<>();
		
		Integer duplicatedBoard = boardService.retrieveDuplicatedBoard(boardName);
		if (duplicatedBoard != null) { //겹치는 게 있으면
			map.put("result", 1);
		} else { //겹치는 게 없으면
			map.put("result", 0);
			//게시판 저장 목록
			BoardVO boardVo = new BoardVO();
			BoardGradeVO boardGradeVo = new BoardGradeVO();

			boardVo.setCategoryNo(categoryNo);
			boardVo.setBoardName(boardName);
			boardVo.setBoardkind(boardkind);
			boardGradeVo.setReadGrade(readGrade);
			boardGradeVo.setWriteGrade(writeGrade);

			this.boardService.registerBoard(boardVo, boardGradeVo);
		}
		return map;
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
	//-------------------------------------------------------------------트랜잭션.. 걸어야되니..?
	@ResponseBody
	@PostMapping("/modifyBoard")
	public Map<String, Integer> modifyBoard(
			@RequestParam("boardNo") int boardNo,
			@RequestParam("categoryNo") int categoryNo,
			@RequestParam("boardName") String boardName,
			@RequestParam("writeGrade") int writeGrade,
			@RequestParam("readGrade") int readGrade,
			@RequestParam("boardkind") int boardkind) {

		Map<String, Integer> map = new HashMap<>();
		
		BoardVO boardVo = new BoardVO();
		BoardGradeVO boardGradeVo = new BoardGradeVO();

		//게시판명 중복 검사
		Integer duplicatedBoard = boardService.retrieveDuplicatedBoard(boardName);
		if (duplicatedBoard == null) { //중복 x
			map.put("result", 0);
			modifyBoardSub(boardVo, boardGradeVo, boardNo, categoryNo,boardName, writeGrade, readGrade, boardkind);
			this.boardService.modifyBoard(boardVo, boardGradeVo);

		} else if(duplicatedBoard != null) { //중복 o
			if (duplicatedBoard == boardNo) { //해당 게시판이면 result 0
				map.put("result", 0);
				modifyBoardSub(boardVo, boardGradeVo, boardNo, categoryNo,boardName, writeGrade, readGrade, boardkind);
				this.boardService.modifyBoard(boardVo, boardGradeVo);
			} else { //다른 게시판이면 result 1
				map.put("result", 1);
			}
		}
		return map;
	}

	//게시판 수정 sub
	public void modifyBoardSub(BoardVO boardVo, BoardGradeVO boardGradeVo,
							   int boardNo, int categoryNo, String boardName, int writeGrade, int readGrade, int boardkind) {
		boardVo.setBoardNo(boardNo);
		boardVo.setCategoryNo(categoryNo);
		boardVo.setBoardName(boardName);
		boardGradeVo.setWriteGrade(writeGrade);
		boardGradeVo.setReadGrade(readGrade);
		boardVo.setBoardkind(boardkind);
	}


	//게시판 삭제
	@ResponseBody
	@PostMapping("/deleteBoard")
	public Map<String, Integer> deleteBoard(@ModelAttribute("boardVo") BoardVO boardVo) {

		int boardNo = boardVo.getBoardNo();
		int count = this.boardService.retrieveConnectArticle(boardNo);

		Map<String, Integer> map = new HashMap<>();

		if (count != 0) {
			int connectArticle = 1;
			map.put("result", connectArticle);
		} else {
			int connectArticle = 0;
			map.put("result", connectArticle);

			this.boardService.removeBoard(boardNo);
		}
		return map;
	} 


	// 게시판 목록 조회
	@GetMapping("/board/{boardNo}/articles")
	public String selectAllNomalArticle(@PathVariable("boardNo") int boardNo, // @RequestParam("memberNo") int memberNo,
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

		// 양 ~
//		int memberNo = 2;
//		int likeBoard = this.boardService.retrieveLikeBoard(memberNo, boardNo);
//		log.info("목록조회하는 게시판의 즐찾 추가된 거 여부 : " + likeBoard);
//		model.addAttribute("likeBorad", likeBoard);
		// ~ 양

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

	//즐겨찾기 게시판 추가
	@ResponseBody
	@PostMapping("/likeBoard")
	public Map<String, Integer> likeBoard(@RequestParam("memberNo") int memberNo, @RequestParam("boardNo") int boardNo) {
		log.info("memberNo : " + memberNo + " boardNo : " + boardNo);

		Map<String, Integer> map = new HashMap<>();

		//즐찾 한 게시판인지 여부
		int likeboard = this.boardService.retrieveLikeBoard(memberNo, boardNo);
		log.info("즐찾에 있나요? : ", likeboard);

		if (likeboard == 0) { //즐찾 안된 게시판이면
			log.info("즐찾에 저장 안 된 애 (0)");
			map.put("result", 0);
			this.boardService.registerLikeBoard(memberNo, boardNo);
		} else { //즐찾게시판면
			log.info("즐찾게시판에 저장된 애 (1)");
			map.put("result", 1);
			this.boardService.removeLikeBoard(memberNo, boardNo);
		}

		log.info("result {} : ", map.get("result").toString());

		return map;
	}

}
