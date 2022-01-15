package com.example.musicat.security;

import com.example.musicat.domain.member.MemberVO;
import com.example.musicat.domain.music.Music;
import lombok.extern.java.Log;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log

// User : Spring Security가 제공하는 UserDetails의 구현체
public class MemberAccount extends User {

    private MemberVO memberVo;

    //private String memberAccountTest;


    public MemberAccount(MemberVO memberVo, Collection<? extends GrantedAuthority> authorities) {

        //회원의 권한을 authorites에 받아서 memberVo에 추가 (기본으로 id, pwd, authorities 받음)
        super(memberVo.getEmail(), memberVo.getPassword(), authorities);
        this.memberVo = memberVo;
        //log.info("---------------MemberAccount extends User : " + memberAccountTest);
        //this.memberAccountTest = memberAccountTest;

    }

    public MemberVO getMemberVo() {
        //log.info("MemberAccount memberVo : " + memberVo.toString());
        return memberVo;
    }

}
