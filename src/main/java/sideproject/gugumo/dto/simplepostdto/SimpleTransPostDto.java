package sideproject.gugumo.dto.simplepostdto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.Location;
import sideproject.gugumo.domain.meeting.MeetingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class SimpleTransPostDto {

    private Long postId;
    private MeetingStatus meetingStatus;
    private GameType gameType;
    private Location location;
    private String title;
    private int meetingMemberNum;
    private LocalDate meetingDeadline;
    private boolean isBookmarked;
}
