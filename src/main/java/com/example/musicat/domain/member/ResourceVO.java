package com.example.musicat.domain.member;


import lombok.*;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Alias("resourceVo")
public class ResourceVO {
    private int resourceNo;
    private String resourceName;
    private String resourceType;
}