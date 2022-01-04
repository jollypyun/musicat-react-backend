package com.example.musicat.controller;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;


@ControllerAdvice
@RestController
public class ExceptionAdvisor {

    @ExceptionHandler(Exception.class)
    public String exceptionError(Exception exception) { // 걸러내지 못하는 것들
        return "error"; //error page로 이동
    }

    //User 세션 만료 Exception
    //SQL Exception(이건 valid 체크만 잘 해두면 할 필요가 없지 않을까..?)
    //@Vaildation 통과 실패 처리 메서드
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String processValidationError(MethodArgumentNotValidException exception, Model model) {

        exception.getMessage();
        return null;
    }

    //첨부파일 maxFileSize 초과 에러
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public void ExceededFileSizeError(FileSizeLimitExceededException exception) {

    }

    //첨부파일 maxRequestSize 초과 에러
    @ExceptionHandler(SizeLimitExceededException.class)
    public void ExceededLimitSizeError(SizeLimitExceededException exception) {

    }

}
