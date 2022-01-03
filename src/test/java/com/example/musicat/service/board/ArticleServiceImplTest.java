//package com.example.musicat.service.board;
//
//import com.example.musicat.domain.board.ArticleVO;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Slf4j
//class ArticleServiceImplTest {
//
//    @Autowired ArticleServiceImpl articleService;
//
//    @Test
//    public void retrieveArticle() throws Exception{
//        //given
//        int articleNo = 50;
//
//        //when
//        ArticleVO fa = articleService.retrieveArticle(articleNo);
//
//        //then
//        log.info(fa.toString());
//        Assertions.assertEquals(articleNo, fa.getNo());
//    }
//}