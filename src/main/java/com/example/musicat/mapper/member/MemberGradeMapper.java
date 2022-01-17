package com.example.musicat.mapper.member;

import com.example.musicat.domain.member.MemberGradeVO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface MemberGradeMapper {
    List<MemberGradeVO> selectMemberGradeList(String email);
}