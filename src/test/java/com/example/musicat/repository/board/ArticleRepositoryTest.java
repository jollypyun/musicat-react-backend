package com.example.musicat.repository.board;

import com.example.musicat.domain.board.Article;
import com.example.musicat.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Slf4j
class ArticleRepositoryTest {

    @Autowired @Qualifier("articleRepository")
    private BaseRepository articleRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void 게시글_등록() throws Exception {
        //given ~가 주어졌을 때
        Article article = Article.createArticle(1, 3, "팩토", "리", "테스트");
        //when 이것을 실행하면
        articleRepository.save(article);
        //then 결과가 이렇게 나와야 한다.
    }

    @Test
    @Transactional(readOnly = true)
    public void 게시글_모두조회() throws Exception {
        //given
        List articles = articleRepository.findAll();
        //when
        //then
    }


}