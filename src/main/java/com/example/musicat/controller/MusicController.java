package com.example.musicat.controller;

import com.example.musicat.service.music.MusicApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;

@RestController
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
    public String upload(@RequestParam(value = "audio") MultipartFile file, @RequestParam(value = "image") MultipartFile imagefile,
                       @RequestParam(value = "title") String title, @RequestParam(value = "memberNo") int memberNo,
                       @RequestParam(value = "articleNo") int articleNo) {
        return musicApiService.registerMusic(file, imagefile, title, memberNo, articleNo);
    }


}
