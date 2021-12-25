package com.example.musicat.repository.board;

import com.example.musicat.domain.board.Article;
import com.example.musicat.domain.board.Board;
import com.example.musicat.domain.member.Member;
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

    @Autowired EntityManager em;

    @Autowired @Qualifier("articleRepository")
    private BaseRepository articleRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void 게시글_등록() throws Exception {
        //given ~가 주어졌을 때
        Member findMember = em.find(Member.class, 3);
        Board findBoard = em.find(Board.class, 2);
//        Article article = Article.builder()
//                .member(findMember)
//                .board(findBoard)
//                .nickname("Test용")
//                .subject("Test용")
//                .content("Test")
//                .build();
        //when 이것을 실행하면
//        articleRepository.save(article);
//        em.persist(article);
        //then 결과가 이렇게 나와야 한다.
        log.info(findMember.toString());
        log.info(findBoard.toString());
    }

    @Test
    public void 게시글_삭제() throws Exception {
        //given

        //when

        //then
    }

    @Test
    public void 게시글_수정() throws Exception {
        //given

        //when

        //then
    }

    @Test
    public void 게시글_단건_조회() throws Exception {
        //given

        //when

        //then
    }

    @Test
    public void 게시글_게시판별_조회() throws Exception {
        //given

        //when

        //then
    }

    @Test
    @Transactional(readOnly = true)
    public void 게시글_모두_조회() throws Exception {
        //given
        List articles = articleRepository.findAll();
        //when
        //then
    }


}

