package com.example.musicat.domain.member;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Alias("memberGradeVo")
public class MemberGradeVO {
    private int memberNo;
    private String grade;
    private String resourceName;
}