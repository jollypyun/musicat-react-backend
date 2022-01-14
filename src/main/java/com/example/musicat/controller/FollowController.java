package com.example.musicat.controller;

import com.example.musicat.service.member.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class FollowController {
    @Autowired
    private FollowService followService;

    @ResponseBody
    @GetMapping("/followingCount/{memberNo}")
    public int followingCount(@PathVariable int memberNo) {
        log.info("시작");
        log.info("no : " + this.followService.countFollowing(memberNo));
        return this.followService.countFollowing(memberNo);
    }

    @ResponseBody
    @GetMapping("/followedCount/{memberNo}")
    public int followedCount(@PathVariable int memberNo) {
        return this.followService.countFollowed(memberNo);
    }

    @GetMapping("/followingList/{memberNo}")
    public String followingList(@PathVariable int memberNo, Model model) {
        int flag = 0;
        model.addAttribute("flag", flag);
        model.addAttribute("followingList", this.followService.retrieveFollowingList(memberNo));
        log.info("msg : " + this.followService.retrieveFollowingList(memberNo));
        return "view/member/listAboutFollow";
    }

    @GetMapping("/followedList/{memberNo}")
    public String followedList(@PathVariable int memberNo, Model model) {
        int flag = 1;
        model.addAttribute("flag", flag);
        model.addAttribute("followedList", this.followService.retrieveFollowedList(memberNo));
        log.info("msg : " + this.followService.retrieveFollowedList(memberNo));
        return "view/member/listAboutFollow";
    }

    @ResponseBody
    @PostMapping("/follow")
    public Map<String, Integer> followSomeone(@RequestParam("opponent") int opponent, @RequestParam("my") int my) {
        this.followService.addFollow(my, opponent);
        Map<String, Integer> map = new HashMap<>();
        map.put("opponent", opponent);
        map.put("my", my);
        map.put("checkFollow", 1);
        return map;
    }

    @ResponseBody
    @PostMapping("/followCancel")
    public Map<String, Integer> cancelSomeone(@RequestParam("opponent") int opponent, @RequestParam("my") int my) {
        this.followService.removeFollow(my, opponent);
        Map<String, Integer> map = new HashMap<>();
        map.put("opponent", opponent);
        map.put("my", my);
        map.put("checkFollow", 0);
        return map;
    }
}
