package sideproject.gugumo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.Location;
import sideproject.gugumo.domain.meeting.MeetingStatus;
import sideproject.gugumo.domain.meeting.MeetingType;

import java.time.LocalDate;
import java.time.LocalDateTime;


//장기, 단기에 따라 dto를 나눠서 전송

@Builder
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  //Null인 필드 제외
public class DetailPostDto {

    private String author;

    private MeetingType meetingType;
    private GameType gameType;
    private int meetingMemberNum;
    private LocalDateTime meetingDateTime;
    private String meetingDays;
    private LocalDate meetingDeadline;
    private String openKakao;
    private Location location;

    private String title;
    private String content;
    private LocalDateTime createdDateTime;

    private MeetingStatus status;
    private long viewCount;

    //북마크 수
    private boolean isYours;
}
