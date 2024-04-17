package sideproject.gugumo.request;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class PostReq {

    private Long authorId;              //토큰에서 가져온다면 이건 필요없을듯

    private String meetingType;
    private String gameType;
    private int meetingMemberNum;
    private LocalDate meetingDate;
    private String meetingTime;
    private LocalDateTime meetingDeadline;
    private String openKakao;
    private String location;


    private String title;
    private String content;

}
