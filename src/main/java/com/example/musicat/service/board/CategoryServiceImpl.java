package com.example.musicat.service.board;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.musicat.domain.board.BoardVO;
import com.example.musicat.domain.board.CategoryBoardVO;
import com.example.musicat.domain.board.CategoryVO;
import com.example.musicat.repository.board.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import lombok.extern.java.Log;

@Log
@Repository("categoryService")
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDao categoryDao;

	@Override
	public void registerCategory(String categoryName) {
		this.categoryDao.insertCategory(categoryName);

	}

	@Override
	public void modifyCategory(int categoryNo, String categoryName) {
		this.categoryDao.updateCategory(categoryNo, categoryName);
	}

	@Override
	public int retrieveConnectBoard(int categoryNo) {
		int checkConnectBoard = this.categoryDao.selectConnectBoard(categoryNo);
		return checkConnectBoard;
	}

	@Override
	public void removeCategory(int categoryNo) {
		log.info("removeCategory-----------");
		this.categoryDao.deleteCategory(categoryNo);
	}

	public ArrayList<CategoryVO> retrieveCategoryList() {
		ArrayList<CategoryVO> categoryList = this.categoryDao.selectCategoryList();
		return categoryList;
	}

	// 카테고리 + 게시판 목록 조회
	@Override
	public ArrayList<CategoryVO> retrieveCategoryBoardList() {
		ArrayList<CategoryBoardVO> list = this.categoryDao.selectCategoryBoardList();

		ArrayList<CategoryVO> categoryList = new ArrayList<CategoryVO>();
		HashMap<String, CategoryVO> checkCategory = new HashMap<String, CategoryVO>();

		CategoryVO category = null;

		for (CategoryBoardVO temp : list) {
			if (checkCategory.containsKey(temp.getCategoryVo().getCategoryName())) {
				category = checkCategory.get(temp.getCategoryVo().getCategoryName());
			} else {
				category = new CategoryVO();
				category.setCategoryNo(temp.getCategoryVo().getCategoryNo());
				category.setCategoryName(temp.getCategoryVo().getCategoryName());
				checkCategory.put(category.getCategoryName(), category);
				categoryList.add(category);
			}
			if (temp.getBoardVo().getBoardName() != null)
				category.addBoardList(new BoardVO(temp.getBoardVo().getBoardNo(), temp.getBoardVo().getBoardName(), temp.getBoardVo().getBoardkind()));
		}
		return categoryList;

	}

	@Override
	public CategoryVO retrieveOneCategory(int categoryNo) {
		CategoryVO categoryVo = this.categoryDao.selectOneCategory(categoryNo);
		return categoryVo;
	}

	@Override
	public Integer retrieveDuplicatedCategory(String categoryName) {
		Integer duplicatedCategory = this.categoryDao.selectDuplicatedCategory(categoryName);
		return duplicatedCategory;
	}
//
//	@Override
//	public ArrayList<CategoryVO> retrieveCategoryList() {
//		ArrayList<CategoryVO> categoryList = this.categoryDao.selectCategoryList();
//		return categoryList;
//	}

}
