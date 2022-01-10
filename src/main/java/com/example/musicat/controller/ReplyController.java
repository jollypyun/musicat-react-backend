package com.example.musicat.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;

import com.example.musicat.domain.etc.NotifyVO;
import com.example.musicat.websocket.manager.NotifyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.musicat.service.board.ReplyService;
import com.example.musicat.domain.board.ReplyVO;
import com.example.musicat.domain.member.MemberVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ReplyController {

	@Autowired
	private ReplyService replyService;

	@Autowired
	private NotifyManager notifyManager;
	
	@ResponseBody
	@PostMapping("/insertReply")
	public List<ReplyVO> insertReply(@RequestParam("articleNo") int articleNo
			,@RequestParam("content") String content
			,HttpServletRequest req) {
		// create
		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		int memberNo = member.getNo();
		String nickname = member.getNickname();
		ReplyVO reply = ReplyVO.createReply(articleNo, memberNo, nickname, content);
		
		// bind
		this.replyService.registerReply(reply); // DB insert
		List<ReplyVO> replyList = this.replyService.retrieveAllReply(articleNo); // select Replys

		// 예나 - 실시간 알림 테스트
		notifyManager.addNotify(new NotifyVO(1, "댓글 알림 테스트", "/main"));
		return replyList;
	}
	
	@ResponseBody
	@PostMapping("/modifyReply")
	public List<ReplyVO> updateReply(@RequestParam("no") int replyNo
			,@RequestParam("articleNo") int articleNo
			,@RequestParam("content") String content){
		ReplyVO reply = ReplyVO.updateReply(replyNo, content);
		// 댓글 수정
		this.replyService.modifyReply(reply);
		// 수정 완료된 댓글 List 받기
		List<ReplyVO> replyList =this.replyService.retrieveAllReply(articleNo);
		return replyList;
	}
	
	@ResponseBody
	@GetMapping("/removeReply")
	public List<ReplyVO> deleteReply(@RequestParam("no") int replyNo
			,@RequestParam("articleNo") int articleNo
			,HttpServletRequest req){
		HttpSession session = req.getSession();
		MemberVO member = (MemberVO) session.getAttribute("loginUser");
		int memberNo = member.getNo();
		this.replyService.removeReply(replyNo, memberNo); // 댓글 삭제
		List<ReplyVO> replyList = this.replyService.retrieveAllReply(articleNo); // 목록 출력
		
		return replyList;
	}

}
