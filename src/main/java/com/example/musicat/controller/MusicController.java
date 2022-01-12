package com.example.musicat.controller;

import com.example.musicat.domain.member.MemberVO;
import com.example.musicat.domain.music.Music;
import com.example.musicat.domain.music.Playlist;
import com.example.musicat.service.music.MusicApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
public class MusicController {

    private final MusicApiService musicApiService;

    @Autowired
    public MusicController(MusicApiService musicApiService) {
        this.musicApiService = musicApiService;
    }

    @GetMapping("musictest/{id}")
    public void musicTest(@PathVariable Long id) {
        System.out.println("musictest entered");
        musicApiService.retrieveMusicById(id);
    }

//    @PostMapping("musicpost")
//    public Music upload(@RequestParam(value = "audio") MultipartFile file, @RequestParam(value = "image") MultipartFile imagefile,
//                        @RequestParam(value = "title") @Size(min = 1, max = 1) String title, @RequestParam(value = "memberNo") int memberNo,
//                        @RequestParam(value = "articleNo") int articleNo) throws HttpClientErrorException {
//        return musicApiService.registerMusic(file, imagefile, title, memberNo, articleNo);
//    }

    @PostMapping("musicpost")
    public ModelAndView upload(@RequestParam(value = "audio") MultipartFile file, @RequestParam(value = "image") MultipartFile imagefile,
                               @RequestParam(value = "title") @Size(min = 1, max = 1) String title
            , @RequestParam(value = "memberNo") int memberNo) throws HttpClientErrorException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("view/board/musicRegisterCheck");

        Music music = musicApiService.registerMusic(file, imagefile, title, memberNo);
        mv.addObject("music", music);
        return mv;
    }

    @DeleteMapping("musicDeleteArticle")
    public String deleteMusicArticle(int articleNo) {
        musicApiService.deleteMusicByArticleNo(articleNo);
        return "success";
    }

    @DeleteMapping("musicDelete")
    public Map<String, String> deleteMusic(Long musicId) {
        musicApiService.deleteByMusicId(musicId);
        Map<String, String> map = new HashMap<String, String>();
        //map.put("result", "hello");
        map.put("success", Integer.toString(1));
        return map;
    }

    // 플레이리스트 추가
    @PostMapping("/addplaylist")
    public ModelAndView addPlaylist(@RequestParam(name = "title") String title, @RequestParam(name = "id") int memberNo, @RequestParam("image") MultipartFile file) {
        ModelAndView mv =  new ModelAndView();
        log.info("title : " + title);
        Playlist playlist = new Playlist();
        playlist.setMemberNo(memberNo); playlist.setPlaylistName(title);
        log.info("playlist : " + playlist.getPlaylistName());
        musicApiService.createPlaylist(playlist, file);
        mv.setView(new RedirectView("/myPage/Playlist/" + memberNo));
        return mv;
    }

    // 플레이리스트 삭제
    @DeleteMapping("deleteplaylist/{memberNo}/{playNo}")
    public ModelAndView removePlaylist(HttpServletRequest req, @PathVariable(name = "playNo") String playNo, @PathVariable(name = "memberNo")Integer memberNo) {
        ModelAndView mv = new ModelAndView();
        //MemberVO member = (MemberVO) req.getSession().getAttribute("principal"); 이게 제대로 된 방법
        log.info("no : " + playNo);
        musicApiService.deletePlaylist(memberNo, playNo);
        mv.setView(new RedirectView("/myPage/Playlist/" + memberNo));
        return mv;
    }

    // 특정 플레이리스트 안에 곡 넣기
    @PostMapping("/pushmusic/{playlistNo}")
    public ModelAndView insertMusicIntoPlaylist(HttpServletRequest req, @RequestParam(name = "musicNos") List<Integer> musicNos, @PathVariable String playlistNo) {
        ModelAndView mv = new ModelAndView();
        //MemberVO member = (MemberVO) req.getSession().getAttribute("principal"); 이게 제대로 된 방법
        int memberNo = 6; // 임시방편
        log.info("musicNo : " + musicNos);
        log.info("playlistNo : " + playlistNo);
        musicApiService.pushMusic(musicNos, playlistNo);
        mv.setView(new RedirectView("/"));
        return mv;
    }

    // 특정 플레이리스트 안의 곡 빼기
    @DeleteMapping("/pullmusic/{playlistNo}")
    public void deleteMusicFromPlaylist(HttpServletRequest req, @PathVariable String playlistNo, @RequestParam(name = "musicNos") List<Integer> musicNos) {
        //MemberVO member = (MemberVO) req.getSession().getAttribute("principal"); 이게 제대로 된 방법
        int memberNo = 6;
        log.info("musicNos : " + musicNos);
        log.info("playlistNo : " + playlistNo);
        musicApiService.pullMusic(musicNos, playlistNo);
    }

    // 플레이리스트 수정
    @PostMapping("/changeplaylist/{playlistNo}")
    public ModelAndView changePlaylistName(@PathVariable String playlistNo, @RequestParam(name = "title") String title, @RequestParam(name="image") MultipartFile image) {
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

    // 플레이리스트 상세 불러오기
    @GetMapping("getDetailPlaylist/{playlistNo}")
    public void getDetailPlaylist(@PathVariable int playlistNo) {
        //MemberVO member = (MemberVO) req.getSession().getAttribute("principal"); 이게 제대로 된 방법
        //String str = musicApiService.showDetailPlaylist(playlistNo);
    }

    // 플레이리스트 썸네일 미리보기 이미지 가져오기
    @GetMapping("/playlistTempImage/")
    public void getThumbnailImage(@RequestParam(name = "playlistNos") List<String> playlistNos){
    }

    @PutMapping("musicConnectArticle/{musicId}/{articleNo}")
    public void connectToArticle(@PathVariable(name="musicId") Long musicId, @PathVariable(name="articleNo") int articleNo) {
        log.info("musicId : " + musicId);
        log.info("articleNo : " + articleNo);

        musicApiService.connectToArticle(musicId, articleNo);
    }

    @GetMapping("retrieveMusics/{articleNo}")
    public List<Music> retrieveMusics(@PathVariable(name="articleNo") int articleNo){
        List<Music> musicList = musicApiService.retrieveMusics(articleNo);
        log.info(musicList.toString());
        return musicList;
    }
}
