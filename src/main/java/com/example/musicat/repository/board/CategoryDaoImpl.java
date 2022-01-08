package com.example.musicat.repository.board;

import java.util.ArrayList;

import com.example.musicat.domain.board.CategoryBoardVO;
import com.example.musicat.domain.board.CategoryVO;
import com.example.musicat.mapper.board.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.musicat.mapper.board.CategoryMapper;
import com.example.musicat.domain.board.CategoryBoardVO;
import com.example.musicat.domain.board.CategoryVO;

import lombok.extern.java.Log;


@Log
@Repository("categoryDao")
public class CategoryDaoImpl implements CategoryDao {

	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public void insertCategory(String categoryName) {
		this.categoryMapper.insertCategory(categoryName);
	}

	@Override
	public void updateCategory(int categoryNo, String categoryName) {
		this.categoryMapper.updateCategory(categoryNo, categoryName);	
	}
	
	@Override
	public int selectConnectBoard(int categoryNo) {
		int checkConnectBoard = this.categoryMapper.selectConnectBoard(categoryNo);
		return checkConnectBoard;
	}

	@Override
	public void deleteCategory(int categoryNo) {
		this.categoryMapper.deleteCategory(categoryNo);
	}

	@Override
	public ArrayList<CategoryVO> selectCategoryList() {
		ArrayList<CategoryVO> categoryList = this.categoryMapper.selectCategoryList();
		return categoryList;
	}
	
	//카테고리 + 게시판 목록 조회
	@Override
	public ArrayList<CategoryBoardVO> selectCategoryBoardList() {
		ArrayList<CategoryBoardVO> categoryBoardList = this.categoryMapper.selectCategoryBoardList();
		return categoryBoardList;
	}
	
	@Override
	public CategoryVO selectOneCategory(int categoryNo) {
		CategoryVO categoryVo = this.categoryMapper.selectOneCategory(categoryNo);
		return categoryVo;
	}

	@Override
	public int selectDuplicatedCategory(String categoryName) {
		int duplicatedCategory = this.categoryMapper.selectDuplicatedCategory(categoryName);
		return duplicatedCategory;
	}




}
