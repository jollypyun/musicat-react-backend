package com.example.musicat.util;

import com.example.musicat.domain.board.ArticleVO;
import com.example.musicat.repository.board.ArticleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class BestArticle {

    @Autowired ArticleDao articleDao;

    public void insertBestArticle(List<ArticleVO> bestArticles) {
        HashMap<String, Object> map = new HashMap<>();
        for (ArticleVO bestArticle : bestArticles) {
            map.put("articleNo", bestArticle.getNo());
            map.put("subject", bestArticle.getSubject());
            map.put("likecount", bestArticle.getLikecount());
            this.articleDao.insertBestArticle(map);
            map.remove("articleNo");
            map.remove("subject");
            map.remove("likecount");
        }
    }

}
