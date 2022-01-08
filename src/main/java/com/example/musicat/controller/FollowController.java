package com.example.musicat.controller;

import com.example.musicat.service.member.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

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
    @PostMapping("/follow/{followedNo}")
    public void followSomeone(@PathVariable int followedNo) {
    }
}
