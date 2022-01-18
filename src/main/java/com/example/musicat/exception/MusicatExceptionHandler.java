package com.example.musicat.exception;

import com.example.musicat.controller.HomeController;
import com.example.musicat.domain.board.CategoryVO;
import com.example.musicat.exception.customException.EmptyFileException;
import com.example.musicat.service.board.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class MusicatExceptionHandler {

    @ExceptionHandler(EmptyFileException.class)
    public ModelAndView handleEmptyFileException(HttpServletRequest req, EmptyFileException e) {

        ModelAndView mv = new ModelAndView();

        log.info("get request URI : " + req.getRequestURI());


        for(PageEnum pe : PageEnum.values()) {
            if( pe.getPageURI().equals(req.getRequestURI()) ) {
                checkReturnPage(pe, mv, e);
                break;
            }
        }
        log.error("", e);
        return mv;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException e) {

        ModelAndView mv = new ModelAndView();

        log.info("get request URI : " + req.getRequestURI());


        for(PageEnum pe : PageEnum.values()) {
            if( pe.getPageURI().equals(req.getRequestURI()) ) {
                checkReturnPage(pe, mv, e);
                break;
            }
        }

        log.error("", e);
        return mv;
    }

    private void checkReturnPage(PageEnum pageEnum, ModelAndView mv, Exception e) {

        String viewPage = ""; // 해당하는 페이지 없을 경우 에러 페이지로 가야하나?

        switch(pageEnum){
            case MUSIC_POST:
                viewPage = "/view/board/musicRegister"; // 포워드
                //viewPage = "redirect:/articles/musicRegister"; // 리다이렉트 (리다이렉트 하면 addobject 값은 get 방식으로 넘어감..)

                mv.setViewName(viewPage);

                break;

        }

        String errorMsg =makeErrorMessage(e.getLocalizedMessage());

        mv.addObject("errorMsg", errorMsg);
        mv.setViewName(viewPage);
        mv.addObject("memberNo", HomeController.checkMemberNo().getNo());
    }

    private String makeErrorMessage(String errorMsg) {

        int index = errorMsg.indexOf(":");
        if(index > -1)
            errorMsg = errorMsg.substring(index + 1, errorMsg.length());

        return errorMsg;
    }
}
