package sideproject.gugumo.dto;

import com.querydsl.core.annotations.QueryProjection;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.Location;
import sideproject.gugumo.domain.meeting.MeetingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SimplePostDto {

    private Long postId;
    private MeetingStatus status;
    private GameType gameType;
    private Location location;
    private String title;
    private LocalDateTime meetingDateTime;
    private String meetingDays;
    private int meetingMemberNum;
    private LocalDate meetingDeadline;
    //북마크 여부: 로그인 한 경우

    @QueryProjection
    public SimplePostDto(Long postId, MeetingStatus status, GameType gameType, Location location, String title, int meetingMemberNum, LocalDate meetingDeadline) {
        this.postId = postId;
        this.status = status;
        this.gameType = gameType;
        this.location = location;
        this.title = title;
/*        this.meetingDateTime = meetingDateTime;
        this.meetingDays = meetingDays;*/
        this.meetingMemberNum = meetingMemberNum;
        this.meetingDeadline = meetingDeadline;
    }
}
