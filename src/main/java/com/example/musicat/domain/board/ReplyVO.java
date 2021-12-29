package com.example.musicat.domain.board;

import org.apache.ibatis.type.Alias;

//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Alias("replyVo")
public class ReplyVO {

    private int no;
    private int articleNo;
    private int memberNo;
    private String nickname;
    private String writeDate;
    private String content;

    //==생성 메소드==//
    public static ReplyVO createReply(int articleNo, int memberNo, String nickname, String content){
        ReplyVO replyVO = new ReplyVO();
        replyVO.articleNo = articleNo;
        replyVO.memberNo = memberNo;
        replyVO.nickname = nickname;
        replyVO.content = content;
        return replyVO;
    }

    //==비즈니스 로직==//
    public static ReplyVO updateReply(int replyNo, String content) {
        ReplyVO replyVO = new ReplyVO();
        replyVO.no = replyNo;
        replyVO.content = content;
        return replyVO;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "no=" + no +
                ", articleNo=" + articleNo +
                ", memberNo=" + memberNo +
                ", nickname='" + nickname + '\'' +
                ", writeDate='" + writeDate + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    protected ReplyVO() {
    }

    public int getNo() {
        return no;
    }

    public int getArticleNo() {
        return articleNo;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public String getNickname() {
        return nickname;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public String getContent() {
        return content;
    }
}
