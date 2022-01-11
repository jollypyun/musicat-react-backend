package com.example.musicat.mapper.member;

import com.example.musicat.domain.member.FollowVO;
import com.example.musicat.domain.member.MemberVO;
import com.example.musicat.domain.member.ProfileVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface FollowMapper {
    int selectFollowingCount(int memberNo);
    int selectFollowedCount(int memberNo);
}
