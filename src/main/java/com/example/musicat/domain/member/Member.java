package com.example.musicat.domain.member;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_no")
    private int no;

    @OneToOne
    private Grade grade;
    private String email;
    private String password;
    private String nickname;

    @Column(name = "regdate", insertable = false, updatable = false, nullable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp regdate;

    private LocalDateTime lastdate;
    private int docs;
    private int comms;
    private int visits;
    private Date ban;
    private int ismember;

    @Override
    public String toString() {
        return "Member{" +
                "no=" + no +
                ", grade=" + grade +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", regdate=" + regdate +
                ", lastdate=" + lastdate +
                ", docs=" + docs +
                ", comms=" + comms +
                ", visits=" + visits +
                ", ban=" + ban +
                ", ismember=" + ismember +
                '}';
    }
}
