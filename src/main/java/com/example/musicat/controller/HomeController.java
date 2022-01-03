package com.example.musicat.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.example.musicat.security.MemberContext;
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
	

//	@GetMapping("/ga")
//	public String iid() {
//		return "vlew/home/asdmsadpo";
//	}
//
//	@PostMapping("/")
//	public String selfOut(@RequestParam("memberNo") int no, @RequestParam("password") String password) {
//		this.memberService.modifyMember(no, password);
//		return "redirect:/login";
//	}
//
//	@ResponseBody
//	@PostMapping("/login")
//	public Map<String, String> login(MemberVO user, HttpSession session) {
//		log.info("login email : " + user.getEmail());
//		Map<String, String> map = new HashMap<String, String>();
//
//		try {
//			user = memberService.login(user.getEmail(), user.getPassword());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		int resultType = 0;
//		String failText = "";
//		if( user == null ) {
//			log.info("로그인 실패");
//			resultType = 0;
//			failText = "아이디 또는 비밀번호가 잘못 입력 되었습니다.";
//		} else if( user.getBan().length() > 0 &&
//				!(LocalDateTime.now().isAfter(
//						LocalDateTime.parse(user.getBan(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
//				){
//			resultType = 2;
//			failText = (user.getBan() + "까지 활동 정지 당한 회원입니다.");
//		} else if( user.getIsMember() == 1) {
//			resultType = 3;
//			failText = "탈퇴한 회원입니다.";
//		}  else if( user.getIsMember() == 2) {
//			resultType = 4;
//			failText = "강제 탈퇴 당한 회원입니다.";
//		}
//		else {
//			resultType = 1;
//			failText = "로그인에 성공했습니다.";
//
//			//HttpSession session = request.getSession();
//			//System.out.println(session);
//			SessionConfig.getSessionidCheck("loginUser", Integer.toString(user.getNo()));
//
//			session.setAttribute("loginUser", user);
//			session.setMaxInactiveInterval(60*60); // 세션 유지 시간 1시간으로 설정
//		}
//
//		map.put("resultType", Integer.toString(resultType));
//		map.put("failText", failText);
//		map.put("url", "/main");
//
//		return map;
//	}
	
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


//	@GetMapping("/logout")
//	public String logout(HttpSession session) {
//		session.invalidate();
//
//		return "redirect:main";
//	}
	
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

}
