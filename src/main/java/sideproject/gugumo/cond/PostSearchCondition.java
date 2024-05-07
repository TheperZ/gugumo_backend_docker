package sideproject.gugumo.cond;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.Location;
import sideproject.gugumo.domain.meeting.MeetingStatus;

@AllArgsConstructor
@Builder
@Getter
public class PostSearchCondition {
    private String q;
    private Location location;
    private GameType gameType;
    private MeetingStatus meetingStatus;
    private SortType sortType;
}
