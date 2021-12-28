package com.example.musicat.service.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.musicat.domain.member.MemberVO;
import com.example.musicat.domain.paging.Criteria;
import com.example.musicat.mapper.member.MemberMapper;
import com.example.musicat.repository.member.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("memberService") // 얘는 서비스다
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberdao;// memberDao랑 연결해주겠다

	@Autowired
	private MemberMapper memberMapper;
	
	
	@Override // 회원가입
	public void registerMember(MemberVO mVo) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		this.memberdao.insertMemberNo(map);
		System.out.println(map.get("no"));
		mVo.setNo((int)map.get("no"));
		System.out.println((int)map.get("no"));
		this.memberdao.insertMember(mVo); //this를 적어주는 이유는 @Autowired 연결 선언해준 memberDao랑 같은 애라는걸 알려주려고 적는 거임 (얘가 얘다)
		
	}

	@Override //회원 자진 탈퇴
	public void modifyMember(int memberNo, String password) {
		memberdao.updateMember(memberNo, password);	//@Autowired해서 memberdao로 씀.

	}
	
	@Override //비밀번호 재설정
	public void modifyPassword(int memNo, String newPassword) {
		memberdao.updatePassword(memNo, newPassword);

	}
	
	@Override
	public MemberVO login(String email, String password) throws Exception {
		MemberVO member = memberdao.selectMember(email, password);

		if (member != null)
			memberdao.updateLastDdate(member.getNo());

		return member;
	}

	@Override
	public void test() {

	}

	@Override // 회원 목록 조회
	public ArrayList<MemberVO> retrieveMemberList(Criteria crt) throws Exception {
		return this.memberMapper.selectMemberList(crt);
	}

	@Override
	public int retrieveTotalMember() throws Exception {
		return this.memberMapper.selectTotalMember();
	}

	@Override // 회원 상세 조회
	public MemberVO retrieveMemberByManager(int no) {
		return this.memberMapper.selectMemberByManager(no);
	}

	@Override // 회원 검색 조회
	public ArrayList<MemberVO> retrieveSearchMember(String keyfield, String keyword) {
		Map<String, Object> map = new HashMap<String, Object>();
	
		map.put("keyword", keyword);


		if (keyfield.equals("email")) {
			return this.memberMapper.selectSearchMemberByEmail(keyword);
		} else {
			return this.memberMapper.selectSearchMemberByGrade(keyword);
		}
	}

	@Override // 회원 검색 총 수
	public int retrieveTotalSearchMember(String keyfield, String keyword) {
		if(keyfield.equals("email")) {
			return this.memberMapper.selectTotalSearchMemberByEmail(keyword);
		}
		else {
			return this.memberMapper.selectTotalSearchMemberByGrade(keyword);
		}
	}

	@Override // 회원의 정지기간 업데이트
	public void modifyBan(String banSelect, int no) {
		if (banSelect.equals("7d")) {
			this.memberMapper.updateBan7days(no);
		} else if (banSelect.equals("1d")) {
			this.memberMapper.updateBan1day(no);
		} else {
			this.memberMapper.updateBan1minute(no);
		}
	}

	@Override // 회원의 강제 탈퇴
	public void modifyMemberByForce(int no) {
		this.memberMapper.updateMemberByForce(no);
	}

	@Override // 회원의 게시글 +1
	public void upMemberDocs(int no) {
		this.memberMapper.plusMemberDocs(no);
	}

	@Override // 회원의 게시글 -1
	public void downMemberDocs(int no) {
		this.memberMapper.minusMemberDocs(no);
	}

	@Override // 회원의 댓글 +1
	public void upMemberComms(int no) {
		this.memberMapper.plusMemberComms(no);
	}

	@Override // 회원의 댓글 -1
	public void downMemberComms(int no) {
		this.memberMapper.minusMemberComms(no);
	}

	@Override
	public MemberVO retreiveMemberProfile(int member_no) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean retrieveEmail(String email) {
		String checkEmail = this.memberdao.selectEmail(email);

		if (checkEmail != null) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public boolean retrieveNickname(String nickname) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<MemberVO> retrieveMemberList(int startRow, int memberPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override

	public void updatePassword(MemberVO memberVo) {
		// TODO Auto-generated method stub
		this.memberdao.updatePassword(memberVo);
  }
  
	public int updateTempPassword(MemberVO mVo) {
		// TODO Auto-generated method stub
		
		int result = 0;
		try {
			result = this.memberdao.updateTempPassword(mVo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return result;
		}
		return result;
	}
}
