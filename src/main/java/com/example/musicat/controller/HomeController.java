package com.example.musicat.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import com.example.musicat.security.MemberContext;

import com.example.musicat.domain.board.BestArticleVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//import com.example.musicat.config.SessionConfig;
import com.example.musicat.service.board.ArticleService;
import com.example.musicat.service.board.CategoryService;
import com.example.musicat.service.member.MemberService;
import com.example.musicat.domain.board.ArticleVO;
import com.example.musicat.domain.board.CategoryVO;
import com.example.musicat.domain.member.MemberVO;

import lombok.extern.java.Log;


@Controller
@Log
public class HomeController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;


    @GetMapping("/")
    public String mainPage(){
        return "redirect:/main";
    }


    @RequestMapping("/musicatlogin")
	public String index(Model model, HttpServletRequest request) {
		log.info("/musicatlogin------------------------------------");

		if(request.getParameter("email") != null ) {
			log.info("로그인 실패 - 이전에 입력한 이메일 : " + request.getParameter("email"));
			model.addAttribute(request.getParameter("email"));
		}
		return "view/member/login";
	}


	@GetMapping("/accessDenideGrade")
	public String accessDenied() {
		log.info("/accessDenideGrade------------------------------------");
		return "view/security/accessDenideGrade";
	}


	@GetMapping("/main")
	public String petopiaMain(Model model, HttpSession session) {

		List<ArticleVO> allArticleList = this.articleService.retrieveAllArticle();
		model.addAttribute("articleList", allArticleList);
		model.addAttribute("HomeContent","fragments/viewMainContent");

		List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
		model.addAttribute("categoryBoardList", categoryList);
		CategoryVO categoryVo = new CategoryVO();
		model.addAttribute("categoryVo", categoryVo);

		MemberVO memberSession = (MemberVO) session.getAttribute("loginUser");
		if (memberSession == null) {
			model.addAttribute("isLogin", 0);
		}
		else {
			model.addAttribute("isLogin", 1);
		}

		return "view/home/viewHomeTemplate";
	}


	
	@GetMapping("/join1")
	public String join(Model model) {
		MemberVO mVo = new MemberVO(); //MemberVO라는 빈칸 양식 종이를 새로 가져올때마다 new 선언
		model.addAttribute("mVo", mVo); //model은 우편부, addAttribute 누군가에게 붙여주는 행동, "member"는 member가 속한이름, member 우편물 내용
		return "view/member/register"; // "view/member/register" 이 주소로 보낸다.
	}
	
	@GetMapping("/ChangePwd")
	public String changepwd() {
		return "view/member/passwordChange";
	}

    @GetMapping("/myPage/Playlist")
    public String myPage(Model model) {

        List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
        model.addAttribute("categoryBoardList", categoryList);

        CategoryVO categoryVo = new CategoryVO();
        model.addAttribute("categoryVo", categoryVo);

        model.addAttribute("HomeContent", "fragments/viewMyPagePlaylist");
        return "view/home/viewHomeTemplate";

    }

    @GetMapping("/myPage/Playlist/{playlistNo}")
    public String myPagePlaylistDetail(Model model, @PathVariable String playlistNo) {

        List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
        model.addAttribute("categoryBoardList", categoryList);

        CategoryVO categoryVo = new CategoryVO();
        model.addAttribute("categoryVo", categoryVo);

        model.addAttribute("HomeContent", "fragments/viewMyPagePlaylistDetail");
        return "view/home/viewHomeTemplate";

    }

    @GetMapping("/myPage/Board")
    public String myPageBoard(Model model) {

        List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
        model.addAttribute("categoryBoardList", categoryList);

        CategoryVO categoryVo = new CategoryVO();
        model.addAttribute("categoryVo", categoryVo);

        model.addAttribute("HomeContent", "fragments/viewMyPageBoard");
        return "view/home/viewHomeTemplate";

    }

    @GetMapping("/myPage/Reply")
    public String myPageReply(Model model) {

        List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
        model.addAttribute("categoryBoardList", categoryList);

        CategoryVO categoryVo = new CategoryVO();
        model.addAttribute("categoryVo", categoryVo);

        model.addAttribute("HomeContent", "fragments/viewMyPageReply");
        return "view/home/viewHomeTemplate";

    }

    @GetMapping("/myPage/Like")
    public String myPageLike(Model model) {

        List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
        model.addAttribute("categoryBoardList", categoryList);

        CategoryVO categoryVo = new CategoryVO();
        model.addAttribute("categoryVo", categoryVo);

        model.addAttribute("HomeContent", "fragments/viewMyPageLike");
        return "view/home/viewHomeTemplate";

    }

	

}
