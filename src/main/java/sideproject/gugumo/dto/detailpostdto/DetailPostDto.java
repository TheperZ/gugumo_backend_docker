package sideproject.gugumo.dto.detailpostdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.Location;
import sideproject.gugumo.domain.meeting.MeetingStatus;
import sideproject.gugumo.domain.meeting.MeetingType;

import java.time.LocalDate;
import java.time.LocalDateTime;


//장기, 단기에 따라 dto를 나눠서 전송

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public abstract class DetailPostDto {

    private String author;

    private MeetingType meetingType;
    private GameType gameType;
    private int meetingMemberNum;
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
    private long bookmarkCount;
}
