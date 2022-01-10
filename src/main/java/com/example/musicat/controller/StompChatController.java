package com.example.musicat.controller;

import com.example.musicat.domain.etc.ChatVO;
import com.example.musicat.domain.member.MemberVO;
import com.example.musicat.websocket.manager.NotifyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
//@RequiredArgsConstructor
public class StompChatController {

    //private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달

    @Autowired
    NotifyManager notifyManager;

    private SimpMessagingTemplate template;

    @PostConstruct
    private void init(){
        template = notifyManager.getTemplate();
    }

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
    @MessageMapping(value = "topic/chat/enter")
    public void enter(ChatVO message){
        log.info("stomp : topic/chat/enter");
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        message.setSystem(true);
        template.convertAndSend("/sub/topic/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping(value = "topic/chat/message")
    public void message(ChatVO message) {
        log.info("stomp : topic/chat/message");
        template.convertAndSend("/sub/topic/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping(value="topic/notify/register")
    public void notifyRegister(Integer no) {
        log.info("stomp : /topic/notify");
        log.info("memberNo : {}", no);
//        template.convertAndSend("/sub/topic/notify/" + member.getNotifyId(),
//                "notify registered : " + member.getNotifyId());
        notifyManager.SendNotify(no);
    }

    @GetMapping(value="/notifySend")
    public String notifySend(){
        log.info("notifySend");

        return "forward:main";
        //String notifyId = notifyManager.getNotifyId(memberNo);
//        if(notifyId != null){
//            template.convertAndSend("/sub/topic/notify/" + notifyId,
//                    "stompCon : notifySend");
//        }
    }
}
