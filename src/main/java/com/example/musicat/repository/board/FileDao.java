package com.example.musicat.repository.board;

import java.util.List;

import com.example.musicat.domain.board.FileVO;

public interface FileDao {
	FileVO selectFile(int fileNo);

	List<FileVO> selectFileList(int articleNo); // 해당 게시글의 파일들 다 불러와

	List<FileVO> selectThumbFile(FileVO file); // 썸네일 추출을 위해 위랑은 조건문이 다르다

	void insertFile(FileVO fileVO); // 추가

	void deleteFile(int FileNo); // 삭제
	
	void allDelete(int articleNo);
}
