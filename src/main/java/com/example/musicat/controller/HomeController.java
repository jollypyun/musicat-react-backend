package com.example.musicat.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import com.example.musicat.controller.form.JoinForm;
import com.example.musicat.domain.board.*;
import com.example.musicat.domain.member.FollowVO;
import com.example.musicat.domain.music.Music;
import com.example.musicat.domain.music.Playlist;

import com.example.musicat.domain.board.BestArticleVO;

import com.example.musicat.security.MemberAccount;
import com.example.musicat.service.board.BoardService;
import com.example.musicat.service.board.ReplyService;
import com.example.musicat.service.member.FollowService;
import com.example.musicat.service.member.GradeService;
import com.example.musicat.service.music.MusicApiService;
import com.example.musicat.util.TemplateModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//import com.example.musicat.config.SessionConfig;
import com.example.musicat.service.board.ArticleService;
import com.example.musicat.service.board.CategoryService;
import com.example.musicat.service.member.MemberService;
import com.example.musicat.domain.member.MemberVO;

import lombok.extern.java.Log;


@Controller
@Log
public class HomeController {


    @Autowired
    private GradeService gradeService;

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private CategoryService categoryService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private FollowService followService;

    @Autowired private MusicApiService musicApiService;
  
    @Autowired
    private static AuthenticationManager authenticationManager;

    @Autowired
    private TemplateModelFactory templateModelFactory;

    @GetMapping("/")
    public String mainPage(){
        return "redirect:/main";
    }

    @RequestMapping("/musicatlogin")
  	public String index(Model model, HttpServletRequest request) {

		if(request.getParameter("email") != null ) {
			log.info("로그인 실패 - 이전에 입력한 이메일 : " + request.getParameter("email"));
			model.addAttribute(request.getParameter("email"));
		}
		return "view/member/login";
	}





//	@PostMapping("/")
//	public String selfOut(@RequestParam("memberNo") int no, @RequestParam("password") String password) {
//		this.memberService.modifyMember(no, password);
//		return "redirect:/musicatlogin";
//	}


    public static MemberVO checkMemberNo() {

        MemberVO member = new MemberVO();

        String auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if(auth.equals("[ROLE_ANONYMOUS]")) {
            member.setGradeNo(4);
            member.setNo(0);
        } else {
            member = ((MemberAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberVo();
        }

        return member;
    }

  @GetMapping("/main")
	public String petopiaMain(Model model) { // //@AuthenticationPrincipal Member member HttpSession session, Authentication authentication

      List<ArticleVO> allArticleList = this.articleService.retrieveAllArticle();
      model.addAttribute("articleList", allArticleList);
      List<BestArticleVO> bestArticles = this.articleService.selectAllBestArticle();
      model.addAttribute("bestArticles", bestArticles);

      model.addAttribute("HomeContent", "fragments/viewMainContent");

      List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
      model.addAttribute("categoryBoardList", categoryList);
      CategoryVO categoryVo = new CategoryVO();
      model.addAttribute("categoryVo", categoryVo);

      MemberVO member = checkMemberNo();
      List<BoardVO> likeBoardList = this.boardService.retrieveLikeBoardList(member.getNo());
      model.addAttribute("likeBoardList", likeBoardList);


//      templateModelFactory.setCurPlaylistModel(model);
//      log.info("setted music : " + model.getAttribute("curPlaylist"));

      //musicApiService.showDetailPlaylist()

      return "view/home/viewHomeTemplate";

  }

        //로그인하지 않은 사용자일 경우 ( 로그인한 사용자 정보 처리는 SecurityConfig.java에서 )
//        String auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
//		if (auth.equals("[ROLE_ANONYMOUS]")) {
//
//            //익명 사용자에게 gradeNo 부여 ( 게시판 접근 시 필요 )
//            int gradeNo = gradeService.retrieveGradeNo(auth);
//            log.info("auth : " + auth + " gradeNo : " + gradeNo);
//
//            MemberVO member = new MemberVO();
//            member.setGrade(auth);
//            member.setGradeNo(gradeNo);
//
//            session.setAttribute("loginUser", member);
//            log.info("익명 사용자 - grade : " + member.getGrade() + " gradeNo : " + member.getGradeNo());

	@GetMapping("/join1")
	public String join(Model model) {
		//MemberVO mVo = new MemberVO(); //MemberVO라는 빈칸 양식 종이를 새로 가져올때마다 new 선언
        JoinForm joinForm = new JoinForm();
        model.addAttribute("form", joinForm); //model은 우편부, addAttribute 누군가에게 붙여주는 행동, "member"는 member가 속한이름, member 우편물 내용
		return "view/member/register"; // "view/member/register" 이 주소로 보낸다.
	}
	
	@GetMapping("/ChangePwd")
	public String changepwd() {
		return "view/member/passwordChange";
	}

    @GetMapping("/myPage/Playlist/{userNo}")
    public String myPage(Model model, @PathVariable int userNo) {
        MemberVO member = new MemberVO();
        FollowVO follow = new FollowVO();
        List<Playlist> playlists = new ArrayList<>();

        try {
            member = memberService.retrieveMemberByManager(userNo);
            follow.setFollowing(followService.countFollowing(userNo));
            follow.setFollowed(followService.countFollowed(userNo));
            playlists = musicApiService.showPlaylist(userNo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<BoardVO> likeBoardList = this.boardService.retrieveLikeBoardList(member.getNo());
        model.addAttribute("likeBoardList", likeBoardList);

        List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
        model.addAttribute("categoryBoardList", categoryList);

        CategoryVO categoryVo = new CategoryVO();
        model.addAttribute("categoryVo", categoryVo);
        model.addAttribute("member", member);
        model.addAttribute("follow", follow);
        model.addAttribute("playlists", playlists);
        log.info("playlists : " + playlists);

        model.addAttribute("HomeContent", "fragments/viewMyPagePlaylist");
        return "view/home/viewHomeTemplate";

    }

    @GetMapping("/myPage/Playlist/{userNo}/{playlistKey}")
    public String myPagePlaylistDetail(Model model, @PathVariable(name = "playlistKey") String playlistKey, @PathVariable(name = "userNo") int userNo) {
        MemberVO member = new MemberVO();
        FollowVO follow = new FollowVO();
        List<Music> musics = null;
        try {
            member = memberService.retrieveMemberByManager(userNo);
            follow.setFollowing(followService.countFollowing(userNo));
            follow.setFollowed(followService.countFollowed(userNo));
            musics = musicApiService.showDetailPlaylist(playlistKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<BoardVO> likeBoardList = this.boardService.retrieveLikeBoardList(member.getNo());
        model.addAttribute("likeBoardList", likeBoardList);

        List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
        model.addAttribute("categoryBoardList", categoryList);

        CategoryVO categoryVo = new CategoryVO();
        model.addAttribute("categoryVo", categoryVo);
        model.addAttribute("member", member);
        model.addAttribute("follow", follow);
        model.addAttribute("musics", musics);

        model.addAttribute("HomeContent", "fragments/viewMyPagePlaylistDetail");
        return "view/home/viewHomeTemplate";

    }


    //작성한 게시글 조회 ------------------- 게시글별 댓글 수 추가하면 좋겠다
    @GetMapping("/myPage/Board/{userNo}")
    public String myPageBoard(Model model, @PathVariable int userNo) {
        MemberVO member = new MemberVO();
        FollowVO follow = new FollowVO();
        try {
            member = memberService.retrieveMemberByManager(userNo);
            follow.setFollowing(followService.countFollowing(userNo));
            follow.setFollowed(followService.countFollowed(userNo));
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<BoardVO> likeBoardList = this.boardService.retrieveLikeBoardList(member.getNo());
        model.addAttribute("likeBoardList", likeBoardList);

        List<ArticleVO> articles = this.articleService.retrieveMyArticle(userNo);
        model.addAttribute("articles", articles);

        List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
        model.addAttribute("categoryBoardList", categoryList);

        CategoryVO categoryVo = new CategoryVO();
        model.addAttribute("categoryVo", categoryVo);

        model.addAttribute("member", member);
        model.addAttribute("follow", follow);
        log.info("대체 뭐가 문젠데 Board " + member.getNo());
        model.addAttribute("HomeContent", "fragments/viewMyPageBoard");
        return "view/home/viewHomeTemplate";

    }

    //작성한 댓글 조회--------------------- 작성자 이름에 링크, 게시글 제목 띄우기
    @GetMapping("/myPage/Reply/{userNo}")
    public String myPageReply(Model model, @PathVariable int userNo) {
        MemberVO member = new MemberVO();
        FollowVO follow = new FollowVO();
        try {
            member = memberService.retrieveMemberByManager(userNo);
            follow.setFollowing(followService.countFollowing(userNo));
            follow.setFollowed(followService.countFollowed(userNo));
            log.info("대체 뭐가 문젠데 Reply " + member.getNo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<ReplyVO> replyListOneMember = this.replyService.retrieveReplyOneMember(userNo);
        model.addAttribute("replyListOneMember", replyListOneMember);
        log.info(replyListOneMember.toString());

        List<BoardVO> likeBoardList = this.boardService.retrieveLikeBoardList(member.getNo());
        model.addAttribute("likeBoardList", likeBoardList);

        List<BoardVO> boardNameList = this.boardService.retrieveBoardNameList();
        model.addAttribute("boardNameList", boardNameList);
        log.info(boardNameList.toString());

        List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
        model.addAttribute("categoryBoardList", categoryList);

        CategoryVO categoryVo = new CategoryVO();
        model.addAttribute("categoryVo", categoryVo);

        model.addAttribute("member", member);

        model.addAttribute("follow", follow);

        model.addAttribute("HomeContent", "fragments/viewMyPageReply");
        return "view/home/viewHomeTemplate";

    }

    //추천 누른 게시글 조회
    @GetMapping("/myPage/Like/{userNo}")
    public String myPageLike(Model model, @PathVariable int userNo) {
        MemberVO member = new MemberVO();
        FollowVO follow = new FollowVO();
        try {
            member = memberService.retrieveMemberByManager(userNo);
            follow.setFollowing(followService.countFollowing(userNo));
            follow.setFollowed(followService.countFollowed(userNo));
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<BoardVO> likeBoardList = this.boardService.retrieveLikeBoardList(member.getNo());
        model.addAttribute("likeBoardList", likeBoardList);

        List<ArticleVO> likeArticle = this.articleService.retrieveMyLikeArticle(userNo);
        model.addAttribute("likeArticle", likeArticle);

        List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
        model.addAttribute("categoryBoardList", categoryList);

        CategoryVO categoryVo = new CategoryVO();
        model.addAttribute("categoryVo", categoryVo);

        model.addAttribute("member", member);
        model.addAttribute("follow", follow);
        model.addAttribute("HomeContent", "fragments/viewMyPageLike");
        return "view/home/viewHomeTemplate";

    }


    // 플레이리스트 추가 폼 요청
    @GetMapping("/addPlaylistForm/{memberNo}")
    public String addPlaylistForm(Model model, @PathVariable("memberNo") int memberNo) {
        model.addAttribute("memberNo", memberNo);
        return "view/etc/createPlaylist";
    }

    @GetMapping("/test")
    public String test(){
        return "view/board/mrtest";
    }

    // 플레이리스트 수정 폼 요청
    @GetMapping("/changePlaylistForm/{id}")
    public String changePlaylist(Model model, @PathVariable("id") String id) {
        Playlist playlist = musicApiService.getOnePlaylist(id);
        log.info("playlist : " + playlist.getPlaylistImage().getId());
        model.addAttribute("playlist", playlist);
        return "view/etc/changePlaylist";
    }

}
