package com.example.musicat.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.example.musicat.service.board.BoardService;
import com.example.musicat.service.board.CategoryService;
import com.example.musicat.service.member.GradeService;
import com.example.musicat.domain.board.BoardBoardGradeVO;
import com.example.musicat.domain.board.BoardGradeVO;
import com.example.musicat.domain.board.BoardVO;
import com.example.musicat.domain.board.CategoryVO;
import com.example.musicat.domain.board.CreateBoardVO;
import com.example.musicat.domain.member.GradeVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private GradeService gradeService;

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
	

	//카테고리 수정
	@PostMapping("/modifyCategory")
	public String modifyCategory(@ModelAttribute("categoryVo") CategoryVO categoryVo) {
		log.info("-----------------------------------------------" + categoryVo.toString());
		int categoryNo = categoryVo.getCategoryNo();
		String categoryName = categoryVo.getCategoryName();
		System.out.println(categoryNo + " " + categoryName);
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
	

	@ResponseBody
	@PostMapping("/selectListAdd")
	public CreateBoardVO selectListAdd() throws Exception {
		CreateBoardVO cbVO = new CreateBoardVO();
		//카테고리 목록
		ArrayList<CategoryVO> categoryList = this.categoryService.retrieveCategoryList();
		//등급 목록
		ArrayList<GradeVO> gradeList = this.gradeService.retrieveGradeList();
		
		//ArrayList<BoardVO> boardkindList = this.boardService.retrieveBoardkind();
		
		//cbVO.setBoardkindList(boardkindList);
		cbVO.setCategoryList(categoryList);
		cbVO.setGradeList(gradeList);
		return cbVO;
	}
	
	
	//게시판 수정 - 카테고리, 등급, 게시판 종류 조회
	@ResponseBody
	@PostMapping("/selectListModify")
	public CreateBoardVO selectListModify(@RequestBody HashMap<String, Object> map ) throws Exception {
		CreateBoardVO cbVO = new CreateBoardVO();
		//카테고리 목록
		ArrayList<CategoryVO> categoryList = this.categoryService.retrieveCategoryList();
		//등급 목록
		ArrayList<GradeVO> gradeList = this.gradeService.retrieveGradeList();
		
		int boardNo = Integer.parseInt((String) map.get("boardNo"));
		
		BoardBoardGradeVO bbg = this.boardService.retrieveOneBoard(boardNo);
		
		
		cbVO.setBbg(bbg);
		cbVO.setCategoryList(categoryList);
		cbVO.setGradeList(gradeList);
		
		log.info("------------------asdfaf : " + cbVO.toString());
		return cbVO;
	}
	
	
	
	//게시판 추가 - 저장
	@PostMapping("/writeBoard")
	public String writeBoard(	
			@RequestParam("category") int categoryNo,
			@RequestParam("boardName") String boardName,
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
	
	//게시판 수정 - 선택된 게시판 정보 조회
	@ResponseBody
	@PostMapping("/selectOneBoard")
	public CreateBoardVO selectOneBoard(@ModelAttribute("boardVo") BoardVO boardVo) throws Exception {
		
		this.boardService.retrieveOneBoard(boardVo.getBoardNo());
		
		CreateBoardVO cbVO = new CreateBoardVO();
		//카테고리 목록
		ArrayList<CategoryVO> categoryList = this.categoryService.retrieveCategoryList();
		//등급 목록
		ArrayList<GradeVO> gradeList = this.gradeService.retrieveGradeList();
		
		cbVO.setCategoryList(categoryList);
		cbVO.setGradeList(gradeList);
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
		
		System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz : " + readGrade);
		System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz : " + writeGrade);
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

			
			
	
	@GetMapping("/nListArticlereq/{boardNo}/{boardkind}")
	public RedirectView amuguna(@PathVariable("boardNo") int boardNo, @PathVariable("boardkind") int boardkind, HttpServletRequest request) {
		RedirectView rv = new RedirectView();
		request.setAttribute("boardNo", boardNo);
		rv.addStaticAttribute("boardKind", boardkind);
		System.out.println("BoardController boardkind --------------------- " + boardkind);
		System.out.println("BoardController boardNo --------------------- " + boardNo);
		rv.setUrl("/nListArticle/" + boardNo);
		return rv;
	}
	
}
