package com.example.musicat.mapper.member;

import com.example.musicat.domain.member.GradeResourceVO;

import java.util.List;

public interface GradeResourceMapper {

    //resource와와 grde 목록 갖고오기
    List<GradeResourceVO> selectGradeResourceList();

    //grade와 resource 추가하기
    void insertGradeResource(GradeResourceVO gradeResourceVo);
}