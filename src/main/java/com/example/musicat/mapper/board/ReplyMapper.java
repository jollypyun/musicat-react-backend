package com.example.musicat.mapper.board;

import java.util.List;

import com.example.musicat.domain.board.ReplyVO;

public interface ReplyMapper {
	
	List<ReplyVO> selectAllReply(int articleNo); // 게시글의 댓글 목록 모두 조회
	
	void insertReply(ReplyVO reply); // 추가
	
	void updateReply(ReplyVO reply); // 수정
	
	void deleteReply(int replyNo); // 삭제
	
}
