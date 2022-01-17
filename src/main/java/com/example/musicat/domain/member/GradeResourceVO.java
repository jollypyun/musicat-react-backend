package com.example.musicat.domain.member;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Alias("gradeResourceVo")
public class GradeResourceVO {
    private String resourceName;
    private String resourceType;
    private int gradeNo;
    private String grade;
}