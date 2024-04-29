package sideproject.gugumo.request;

import lombok.Getter;

import java.time.LocalDate;


@Getter
public class UpdatePostReq {



    private String meetingType;
    private String gameType;
    private int meetingMemberNum;
    private LocalDate meetingDate;
    private int meetingTime;
    private String meetingDays;
    private LocalDate meetingDeadline;
    private String openKakao;
    private String location;


    private String title;
    private String content;
    private String meetingStatus;

}