package sideproject.gugumo.request;

import lombok.Getter;
import sideproject.gugumo.domain.meeting.MeetingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * CreatePostReq와 status 차이
 * 중복되는 필드가 많음
 * 그렇다고 한데 묶는게 옳은 선택인가?
 */

@Getter
public class UpdatePostReq {

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
    private String status;

}