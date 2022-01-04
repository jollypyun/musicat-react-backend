package com.example.musicat.service.board;

import java.io.IOException;
import java.util.List;

import com.example.musicat.domain.board.ArticleVO;
import com.example.musicat.domain.board.FileVO;

public interface FileService {

	FileVO selectOneFile(int fileNo);

	void uploadFile(ArticleVO article);
	ArticleVO fileList(ArticleVO article, List<FileVO> files);
	FileVO retrieveThumbFile(FileVO file);
	void removeFile(int fileNo);
	
	void allDelete(int articleNo);

	List<FileVO> selectArticleFiles(int articleNo);
}
