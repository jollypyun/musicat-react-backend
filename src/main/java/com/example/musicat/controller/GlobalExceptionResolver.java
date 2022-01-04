package com.example.musicat.controller;


import lombok.extern.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
@Slf4j
public class GlobalExceptionResolver {

    @ExceptionHandler({Exception.class, Throwable.class})
    public String handleException(HttpServletRequest request, Exception exception, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String statusMsg = status.toString();

        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(statusMsg));
        model.addAttribute("msg", statusMsg + " " + httpStatus.getReasonPhrase());
        log.error("exception : {}", exception.getMessage());
        model.addAttribute("exception", exception);
        return "error";
    }

}




























