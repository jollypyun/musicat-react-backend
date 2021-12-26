package com.example.musicat.service.board;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ArticleService {

    public void save(Object data);

    public Object findOne(Integer id);

    public List findAll();

    public void update(Object data);

    public void remove(Integer id);
}
