package com.example.musicat.mapper.member;

import org.springframework.core.io.Resource;

import java.util.List;

public interface ResourceMapper {
    List<Resource> selectResourceList();
}