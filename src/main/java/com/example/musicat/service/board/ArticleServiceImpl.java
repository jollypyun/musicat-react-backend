package com.example.musicat.service.board;

import com.example.musicat.repository.BaseRepository;
import com.example.musicat.repository.board.ArticleRepositoryImpl;
import com.example.musicat.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("articleService")
@Transactional(readOnly = true)
//@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepositoryImpl articleRepository;

//    private final ArticleRepositoryImpl articleRepository;

    @Override
    @Transactional
    public void save(Object data) {
        articleRepository.save(data);
    }

    @Override
    public Object findOne(Integer id) {
        return null;
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public void update(Object data) {}

    @Override
    public void remove(Integer id) {}
}
