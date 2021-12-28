package com.example.musicat.repository.board;

import com.example.musicat.domain.board.ReplyVO;

import java.util.List;


public interface ReplyDao {
	List<ReplyVO> selectAllReply(int articleNo); //세부 조회
	
	void insertReply(ReplyVO reply); // 추가
	
	void updateReply(ReplyVO reply); // 수정
	
	void deleteReply(int replyNo); // 삭제
	
	void allDelete(int articleNo);

	
}
