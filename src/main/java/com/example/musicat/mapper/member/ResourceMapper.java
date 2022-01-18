package com.example.musicat.mapper.member;

import com.example.musicat.domain.member.GradeResourceVO;
import com.example.musicat.domain.member.MemberGradeVO;
import com.example.musicat.domain.member.ResourceVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ResourceMapper {

    GradeResourceVO selectOneGradeResource(int gradeResourceNo);

    GradeResourceVO selectLastOneGradeResource();

    //grde와 resource 목록 조회
    List<GradeResourceVO> selectGradeResourceList();

    //grade와 resource 추가
    void insertGradeResource(GradeResourceVO gradeResourceVo);

    //grade와 resource 중복 검사
    Map<String, Integer> selectDuplicatedResource(String resourceName, int gradeNo);

    //grade와 resource 수정
    void updateGradeResource(GradeResourceVO gradeResourceVo);

    //grade와 resource 삭제
    void deleteGradeResource(int gradeResourceNo);

    //리소스 목록 조회
    List<ResourceVO> selectResourceList();

    //회원별 등급 및 접근 가능 리소스 조회
    List<MemberGradeVO> selectMemberGradeList(String email);
}