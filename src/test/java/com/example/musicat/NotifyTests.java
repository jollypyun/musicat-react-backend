package com.example.musicat;

import com.example.musicat.domain.etc.NotifyVO;
import com.example.musicat.websocket.manager.NotifyManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class NotifyTests {
    @Autowired
    NotifyManager manager;

    @Test
    void contextLoads() {
		Assertions.assertNotNull(manager);

		NotifyVO notifyVO = new NotifyVO();
		notifyVO.setContent("알림링크테스트");
		notifyVO.setMember_no(1);
		notifyVO.setLink("articles/35");
		manager.addNotify(notifyVO);


        /////////////////////////////////////
		List<NotifyVO> list = manager.getNotifyService().selectNotify(1);

		for(NotifyVO noti : list) {
			System.out.println(noti.toString());
		}

        //manager.updateNotifyRead(1);
    }
}
