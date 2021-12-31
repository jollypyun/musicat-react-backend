package com.example.musicat.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.musicat.config.SessionConfig;
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

//	user security 기본 id : user / password : 콘솔 Using generated security password: ex)75f93c5c-c13c-4bcf-aa11-b9c47b0f899a

    @Autowired
    private MemberService memberService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private ArticleService articleService;


    @GetMapping("/petopialogin")
    public String index() {
        return "view/member/login";
    }


    @GetMapping("/ga")
    public String iid() {
        return "vlew/home/asdmsadpo";
    }

//	@PostMapping("/")
//	public String selfOut(@RequestParam("memberNo") int no, @RequestParam("password") String password) {
//		this.memberService.modifyMember(no, password);
//		return "redirect:/login";
//	}


    @ResponseBody
    @PostMapping("/login")
    public Map<String, String> login(MemberVO user, HttpSession session) {
        log.info("login email : " + user.getEmail());
        Map<String, String> map = new HashMap<String, String>();

        try {
            user = memberService.login(user.getEmail(), user.getPassword());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int resultType = 0;
        String failText = "";
        if (user == null) {
            log.info("로그인 실패");
            resultType = 0;
            failText = "아이디 또는 비밀번호가 잘못 입력 되었습니다.";
        } else if (user.getBan().length() > 0 &&
                !(LocalDateTime.now().isAfter(
                        LocalDateTime.parse(user.getBan(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
        ) {
            resultType = 2;
            failText = (user.getBan() + "까지 활동 정지 당한 회원입니다.");
        } else if (user.getIsMember() == 1) {
            resultType = 3;
            failText = "탈퇴한 회원입니다.";
        } else if (user.getIsMember() == 2) {
            resultType = 4;
            failText = "강제 탈퇴 당한 회원입니다.";
        } else {
            resultType = 1;
            failText = "로그인에 성공했습니다.";

            //HttpSession session = request.getSession();
            //System.out.println(session);
            SessionConfig.getSessionidCheck("loginUser", Integer.toString(user.getNo()));

            session.setAttribute("loginUser", user);
            session.setMaxInactiveInterval(60 * 60); // 세션 유지 시간 1시간으로 설정
        }

        map.put("resultType", Integer.toString(resultType));
        map.put("failText", failText);
        map.put("url", "/main");

        return map;
    }

    @GetMapping("/main")
    public String petopiaMain(Model model, HttpSession session) {
        List<ArticleVO> allArticleList = this.articleService.retrieveAllArticle();
        model.addAttribute("articleList", allArticleList);
        model.addAttribute("HomeContent", "fragments/viewMainContent");
        List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
        model.addAttribute("categoryBoardList", categoryList);
        CategoryVO categoryVo = new CategoryVO();
        model.addAttribute("categoryVo", categoryVo);
        MemberVO member = (MemberVO) session.getAttribute("loginUser");
        if (member == null)
            model.addAttribute("isLogin", 0);
        else
            model.addAttribute("isLogin", 1);
        return "view/home/viewHomeTemplate";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:main";
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

	
	@ResponseBody
	@PostMapping("/login")
	public Map<String, String> login(MemberVO user, HttpSession session) {
		log.info("login email : " + user.getEmail());
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			user = memberService.login(user.getEmail(), user.getPassword());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int resultType = 0;
		String failText = "";
		if( user == null ) {
			log.info("로그인 실패");
			resultType = 0;
			failText = "아이디 또는 비밀번호가 잘못 입력 되었습니다.";
		} else if( user.getBan().length() > 0 &&
				!(LocalDateTime.now().isAfter(
						LocalDateTime.parse(user.getBan(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))) 
				){
			resultType = 2;
			failText = (user.getBan() + "까지 활동 정지 당한 회원입니다.");
		} else if( user.getIsMember() == 1) {
			resultType = 3;
			failText = "탈퇴한 회원입니다.";
		}  else if( user.getIsMember() == 2) {
			resultType = 4;
			failText = "강제 탈퇴 당한 회원입니다.";
		}
		else {
			resultType = 1;
			failText = "로그인에 성공했습니다.";
			
			//HttpSession session = request.getSession();
			//System.out.println(session);
			SessionConfig.getSessionidCheck("loginUser", Integer.toString(user.getNo()));
			
			session.setAttribute("loginUser", user);
			session.setMaxInactiveInterval(60*60); // 세션 유지 시간 1시간으로 설정
		}
		
		map.put("resultType", Integer.toString(resultType));
		map.put("failText", failText);
		map.put("url", "/main");
		
		return map;
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
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		for (CategoryVO categoryVO : categoryList) {
			log.info(categoryVO.toString());
		}
		if(member == null)
			model.addAttribute("isLogin", 0);
		else
			model.addAttribute("isLogin", 1);
		return "view/home/viewHomeTemplate";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:main";
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

}
