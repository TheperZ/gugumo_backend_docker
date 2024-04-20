package sideproject.gugumo.dto;

import com.querydsl.core.annotations.QueryProjection;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.Location;
import sideproject.gugumo.domain.meeting.MeetingStatus;

import java.time.LocalDateTime;

public class SimplePostDto {

    private Long postId;
    private MeetingStatus status;
    private GameType gameType;
    private Location location;
    private String title;
    private LocalDateTime meetingDateTime;
    //요일: meetingType=LONG
    private int meetingMemberNum;
    private LocalDateTime meetingDeadline;
    //북마크 여부

    @QueryProjection

    public SimplePostDto(Long postId, MeetingStatus status, GameType gameType, Location location, String title, LocalDateTime meetingDateTime, int meetingMemberNum, LocalDateTime meetingDeadline) {
        this.postId = postId;
        this.status = status;
        this.gameType = gameType;
        this.location = location;
        this.title = title;
        this.meetingDateTime = meetingDateTime;
        this.meetingMemberNum = meetingMemberNum;
        this.meetingDeadline = meetingDeadline;
    }
}
