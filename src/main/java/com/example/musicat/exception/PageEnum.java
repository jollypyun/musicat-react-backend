package com.example.musicat.exception;

import lombok.Getter;

@Getter
public enum PageEnum {

    //WRITE_BOARD(10, "/wirteboard"),
    MUSIC_POST(11, "/musicpost");

    private int pageCode;
    private final String pageURI;

    PageEnum(int pageCode, String pageURI) {
        this.pageCode = pageCode;
        this.pageURI = pageURI;
    }
//    INVALID_INPUT_VALUE(400," Invalid Input Value"),
//    METHOD_NOT_ALLOWED(405,  "Invalid Input Value"),
//    HANDLE_ACCESS_DENIED(403, "Access is Denied"),
//    EMAIL_DUPLICATION(400, "Email is Duplication"),
//    LOGIN_INPUT_INVALID(400, "Login input is invalid"),
//    INTERNAL_SERVER_ERROR(500, "Internal Server error"),
//    INVALID_USER_ID(400, "Invalid user id");
//
//    private int statusCode;
//    private final String message;
//
//
//    ErrorCode(final int statusCode, final String message) {
//        this.statusCode = statusCode;
//        this.message = message;
//    }
}
