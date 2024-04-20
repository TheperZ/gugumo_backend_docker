package sideproject.gugumo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.Location;
import sideproject.gugumo.domain.meeting.MeetingStatus;
import sideproject.gugumo.domain.meeting.MeetingType;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class DetailPostDto {

    private MeetingType meetingType;
    private GameType gameType;
    private int meetingMemberNum;
    private LocalDateTime meetingDateTime;
    private LocalDateTime meetingDeadline;
    private String openKakao;
    private Location location;


    private String title;
    private String content;

    private MeetingStatus status;
    private long viewCount;
}
