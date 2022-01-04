package com.example.musicat.service.board;

import com.example.musicat.domain.board.ReplyVO;

import java.util.List;


public interface ReplyService {
	
	List<ReplyVO> retrieveAllReply(int articleNo); //세부 조회
	
	void registerReply(ReplyVO reply); // 추가
	
	void modifyReply(ReplyVO reply); // 수정
	
	void removeReply(int replyNo, int memberNo); // 삭제
	
	void allDelete(int articleNo);
}
