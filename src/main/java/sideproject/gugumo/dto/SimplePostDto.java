package sideproject.gugumo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.Location;
import sideproject.gugumo.domain.meeting.MeetingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  //Null인 필드 제외
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
    private boolean isBookmarked;


    @QueryProjection
    public SimplePostDto(Long postId, MeetingStatus status, GameType gameType, Location location, String title, LocalDateTime meetingDateTime, String meetingDays, int meetingMemberNum, LocalDate meetingDeadline, boolean isBookmarked) {
        this.postId = postId;
        this.status = status;
        this.gameType = gameType;
        this.location = location;
        this.title = title;
        this.meetingDateTime = meetingDateTime;
        this.meetingDays = meetingDays;
        this.meetingMemberNum = meetingMemberNum;
        this.meetingDeadline = meetingDeadline;
        this.isBookmarked = isBookmarked;
    }
}
