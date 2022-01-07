package com.example.musicat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller @CrossOrigin
public class ChatController {

    @GetMapping("/chat/{username}")
    public String chatGET(@PathVariable("username") String username, Model model) {

        System.out.println("@ChatController, chatGET() user name : " + username);

        model.addAttribute("username", username);
        return "view/member/chat";
    }
}
