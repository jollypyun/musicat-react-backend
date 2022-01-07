package com.example.musicat.exception;

import lombok.Getter;

@Getter
public enum PageEnum {


    //WRITE_CATEGORY(9, "/writeCategory"),
    WRITE_BOARD(10, "/wirteBoard"),
    MUSIC_POST(11, "/musicpost");


    private int pageCode;
    private final String pageURI;

    PageEnum(int pageCode, String pageURI) {
        this.pageCode = pageCode;
        this.pageURI = pageURI;
    }

}
