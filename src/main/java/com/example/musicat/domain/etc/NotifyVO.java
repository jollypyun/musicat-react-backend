package com.example.musicat.domain.etc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@Alias("notifyVo")
public class NotifyVO {
    private int notify_no;
    private int member_no;
    private String content;
    private String sendDate;
    private String readDate;
    private String link;
}
