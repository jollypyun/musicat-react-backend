package com.example.musicat.service;

import java.util.List;

public interface BaseService<T> {

    /** 등록 */
    public void save(T data);
    /** 1건 조회 */
    public T findOne(Integer id);
    /** 모두 조회 */
    public List<T> findAll();
    /** 수정 */
    public void update(T data);
    /** 삭제 */
    public void remove(Integer id);

}
