package sideproject.gugumo.cond;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.Location;
import sideproject.gugumo.domain.meeting.MeetingStatus;
import sideproject.gugumo.domain.meeting.MeetingType;
import sideproject.gugumo.dto.SimplePostDto;

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
