package sideproject.gugumo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CreatePostReq {


    private String meetingType;     //단기, 장기
    private String gameType;
    private int meetingMemberNum;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate meetingDate;      //날짜: 단기일 경우
    private String meetingDays;     //요일: 장기일 경우
    private int meetingTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate meetingDeadline;
    private String openKakao;
    private String location;


    private String title;
    private String content;

}
