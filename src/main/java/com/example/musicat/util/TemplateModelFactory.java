package com.example.musicat.util;

import com.example.musicat.domain.board.BoardVO;
import com.example.musicat.domain.board.CategoryVO;
import com.example.musicat.domain.member.MemberVO;
import com.example.musicat.domain.music.Music;
import com.example.musicat.security.MemberAccount;
import com.example.musicat.service.board.BoardService;
import com.example.musicat.service.board.CategoryService;
import com.example.musicat.service.music.MusicApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TemplateModelFactory {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private MusicApiService musicApiService;

    private final String CUR_PLAYLIST_KEY = "pl1";

    public static MemberVO getUserMemberVO() {
        MemberVO member = null;

        String auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if(auth.equals("[ROLE_ANONYMOUS]") == false) {
            member = ((MemberAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberVo();
        }

        return member;
    }

    public Model setSideViewModel(Model model){
        List<CategoryVO> categoryList = this.categoryService.retrieveCategoryBoardList();
        model.addAttribute("categoryBoardList", categoryList);
        CategoryVO categoryVo = new CategoryVO();
        model.addAttribute("categoryVo", categoryVo);

        MemberVO member = getUserMemberVO();
        List<BoardVO> likeBoardList = this.boardService.retrieveLikeBoardList(member.getNo());
        model.addAttribute("likeBoardList", likeBoardList);
        return model;
    }

    public Model setCurPlaylistModel(Model model){

        List<Music> musics = null;

        MemberVO member = getUserMemberVO();
        if(member != null) {
            log.info("user logged in");
            musics = musicApiService.showDetailPlaylist(member.getNo() + CUR_PLAYLIST_KEY);
            log.info(musics.toString());
        }
        model.addAttribute("curPlaylist", musics);
        log.info("setted music : " + model.getAttribute("curPlaylist"));
        return model;
    }

    public boolean checkIsUserLogin(){
        return getUserMemberVO() != null;
    }
}
