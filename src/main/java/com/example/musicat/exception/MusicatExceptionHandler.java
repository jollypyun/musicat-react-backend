package com.example.musicat.exception;

import com.example.musicat.domain.board.CategoryVO;
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
        return mv;
    }

    private void checkReturnPage(PageEnum pageEnum, ModelAndView mv, ConstraintViolationException e) {

        String viewPage = ""; // 해당하는 페이지 없을 경우 에러 페이지로 가야하나?

        switch(pageEnum){
            case MUSIC_POST:
                viewPage = "/view/board/musicRegister"; // 포워드
                //viewPage = "redirect:/articles/musicRegister"; // 리다이렉트 (리다이렉트 하면 addobject 값은 get 방식으로 넘어감..)
                break;

        }

        mv.addObject("errorMsg", e.getLocalizedMessage());
        mv.setViewName(viewPage);
    }

}
