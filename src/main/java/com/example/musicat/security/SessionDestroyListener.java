package com.example.musicat.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

//세션 만료 시
@Slf4j
@Component
public class SessionDestroyListener implements ApplicationListener<SessionDestroyedEvent> {

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        List<SecurityContext> securityContextsList = event.getSecurityContexts();

        for(SecurityContext securityContext : securityContextsList) {
            MemberAccount memberAccount = (MemberAccount) securityContext.getAuthentication().getPrincipal();

            log.info("세션 종료 회원 번호 : " + memberAccount.getMemberVo().getNo());
            log.info("세션 종료 회원 이메일 : " + memberAccount.getMemberVo().getEmail());


        }
    }
}