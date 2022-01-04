package com.example.musicat.controller;

import com.example.musicat.service.music.MusicApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
