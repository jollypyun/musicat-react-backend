package com.example.musicat.mapper.board;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.musicat.domain.board.FileVO;

@Mapper
public interface FileMapper {
	
	FileVO selectFile(int fileNo);
	List<FileVO> selectFileList(int articleNo);  // 해당 게시글의 파일들 다 불러와
	void insertFile(FileVO fileVO);  // 추가
	void updateFile(FileVO file); // 수정

	List<FileVO> selectThumbFile(FileVO fileVo);
	
	 void deleteFile(int FileNo); // 삭제
	 
	 void allDelete(int articleNo);
}
