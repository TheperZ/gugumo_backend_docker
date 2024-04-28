package sideproject.gugumo.request;

import lombok.Getter;
import sideproject.gugumo.domain.meeting.MeetingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;



@Getter
public class UpdatePostReq {

    private Long authorId;              //토큰에서 가져온다면 이건 필요없을듯

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
    private String status;

}