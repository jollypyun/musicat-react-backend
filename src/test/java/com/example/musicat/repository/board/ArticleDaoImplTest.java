package com.example.musicat.repository.board;

import com.example.musicat.domain.board.SelectArticleVO;
import com.example.musicat.mapper.board.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ArticleDaoImplTest {

    @Autowired ArticleDaoImpl articleDao;
    @Autowired FileMapper fm;

    @Test
    public void selectArticle() throws Exception{
        //given
        int articleNo = 50;

        //when
        List<SelectArticleVO> findArticle = articleDao.selectArticle(articleNo);

        //then
        for (SelectArticleVO sa : findArticle) {
            log.info(sa.toString());
        }
        Assertions.assertEquals(articleNo, findArticle.get(0).getArticle().getNo());
    }

    @Test
    void selectBoard() {
        fm.deleteFile(22);
    }

    @Test
    void insertArticle() {
    }

    @Test
    void updateArticle() {
    }

    @Test
    void deleteArticle() {
    }

    @Test
    void upViewcount() {
    }

    @Test
    void selectAllArticle() {
    }

    @Test
    void insertLike() {
    }

    @Test
    void deleteLike() {
    }

    @Test
    void totalRecCount() {
    }

    @Test
    void likeCheck() {
    }

    @Test
    void upLikecount() {
    }

    @Test
    void downLikecount() {
    }
}