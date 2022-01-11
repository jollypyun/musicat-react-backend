package com.example.musicat.controller;

import com.example.musicat.domain.member.MemberVO;
import com.example.musicat.domain.music.Music;
import com.example.musicat.domain.music.Playlist;
import com.example.musicat.service.music.MusicApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
public class MusicController {

    public MusicApiService musicApiService;

    @Autowired
    public MusicController(MusicApiService musicApiService) {
        this.musicApiService = musicApiService;
    }

    @GetMapping("musictest/{id}")
    public void musicTest(@PathVariable Long id) {
        System.out.println("musictest entered");
        musicApiService.retrieveMusicById(id);
    }

    @PostMapping("musicpost")
    public Music upload(@RequestParam(value = "audio") MultipartFile file, @RequestParam(value = "image") MultipartFile imagefile,
                        @RequestParam(value = "title") String title, @RequestParam(value = "memberNo") int memberNo,
                        @RequestParam(value = "articleNo") int articleNo) {
        return musicApiService.registerMusic(file, imagefile, title, memberNo, articleNo);
    }

    @DeleteMapping("musicdelete/{articleNo}")
    public void deleteMusic(@PathVariable("articleNo") int articleNo) {
        musicApiService.deleteMusic(articleNo);
    }

    // 플레이리스트 추가
    @PostMapping("/addplaylist")
    public ModelAndView addPlaylist(HttpServletRequest req, @RequestParam(name = "title") String title, @RequestParam(name = "id") int memberNo) {
        ModelAndView mv =  new ModelAndView();
        log.info("title : " + title);
        Playlist playlist = new Playlist();
        playlist.setMemberNo(memberNo); playlist.setPlaylistName(title);
        log.info("playlist : " + playlist.getPlaylistName());
        musicApiService.createPlaylist(playlist);
        mv.setView(new RedirectView("/myPage/Playlist/" + memberNo));
        return mv;
    }

    // 플레이리스트 삭제
    @DeleteMapping("deleteplaylist/{memberNo}/{playNo}")
    public ModelAndView removePlaylist(HttpServletRequest req, @PathVariable(name = "playNo") int playNo, @PathVariable(name = "memberNo")Integer memberNo) {
        ModelAndView mv = new ModelAndView();
        //MemberVO member = (MemberVO) req.getSession().getAttribute("principal"); 이게 제대로 된 방법
        log.info("no : " + playNo);
        musicApiService.deletePlaylist(memberNo, playNo);
        mv.setView(new RedirectView("/myPage/Playlist/" + memberNo));
        return mv;
    }

    // 특정 플레이리스트 안에 곡 넣기
    @PostMapping("/pushmusic/{playlistNo}")
    public void insertMusicIntoPlaylist(HttpServletRequest req, @RequestParam(name = "musicNos") List<Integer> musicNos, @PathVariable int playlistNo) {
        //MemberVO member = (MemberVO) req.getSession().getAttribute("principal"); 이게 제대로 된 방법
        int memberNo = 6; // 임시방편
        log.info("musicNo : " + musicNos);
        log.info("playlistNo : " + playlistNo);
        musicApiService.pushMusic(musicNos, playlistNo);
    }

    // 특정 플레이리스트 안의 곡 빼기
    @DeleteMapping("/pullmusic/{playlistNo}")
    public void deleteMusicFromPlaylist(HttpServletRequest req, @PathVariable int playlistNo, @RequestParam(name = "musicNos") List<Integer> musicNos) {
        //MemberVO member = (MemberVO) req.getSession().getAttribute("principal"); 이게 제대로 된 방법
        int memberNo = 6;
        log.info("musicNos : " + musicNos);
        log.info("playlistNo : " + playlistNo);
        musicApiService.pullMusic(musicNos, playlistNo);
    }

    // 플레이리스트 수정
    @PostMapping("/changeplaylist/{playlistNo}")
    public ModelAndView changePlaylistName(@PathVariable int playlistNo, @RequestParam(name = "title") String title, @RequestParam(name="image") MultipartFile image) {
        ModelAndView mv = new ModelAndView();
        //MemberVO member = (MemberVO) req.getSession().getAttribute("principal"); 이게 제대로 된 방법
        //int memberNo = 6;
        log.info("playlistNo : " + playlistNo);
        Playlist playlist = musicApiService.updatePlaylistName(playlistNo, title, image);
        int memberNo = playlist.getMemberNo();
        mv.setView(new RedirectView("/myPage/Playlist/" + memberNo));
        return mv;
    }

    // 플레이리스트 목록 불러오기
    @GetMapping("/getPlaylist/{memberNo}")
    public List<Playlist> getPlaylist(@PathVariable int memberNo) {
        log.info("member num : " + memberNo);
        List<Playlist> list = musicApiService.showPlaylist(memberNo);
        return list;
    }
//
//    // 플레이리스트 상세 불러오기
//    @GetMapping("getDetailPlaylist/{playlistNo}")
//    public void getDetailPlaylist(@PathVariable int playlistNo) {
//        //MemberVO member = (MemberVO) req.getSession().getAttribute("principal"); 이게 제대로 된 방법
//        String str = musicApiService.showDetailPlaylist(playlistNo);
//    }

    // 플레이리스트 썸네일 미리보기 이미지 가져오기
    @GetMapping("/playlistTempImage/")
    public void getThumbnailImage(@RequestParam(name = "playlistNos") List<Integer> playlistNos){

    }
}
