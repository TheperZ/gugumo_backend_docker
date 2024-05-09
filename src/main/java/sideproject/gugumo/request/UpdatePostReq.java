package sideproject.gugumo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.MeetingStatus;
import sideproject.gugumo.domain.meeting.MeetingType;
import sideproject.gugumo.validate.Conditional;
import sideproject.gugumo.validate.EnumValue;

import java.time.LocalDate;


@Getter
@Conditional.List(
        {
                @Conditional(
                        selected = "meetingType",
                        values = "SHORT",
                        required = "meetingDate"
                ),
                @Conditional(
                        selected = "meetingType",
                        values = "LONG",
                        required = "meetingDays"
                )
        }
)
public class UpdatePostReq {


    @NotEmpty
    @EnumValue(enumClass = MeetingType.class)
    private String meetingType;
    @NotEmpty
    @EnumValue(enumClass = GameType.class)
    private String gameType;
    @NotNull
    private int meetingMemberNum;
    private LocalDate meetingDate;      //단기
    @NotNull
    private int meetingTime;
    private String meetingDays;         //장기
    @NotNull
    private LocalDate meetingDeadline;
    @NotEmpty
    private String openKakao;
    @NotEmpty
    private String location;

    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotEmpty
    @EnumValue(enumClass = MeetingStatus.class)
    private String meetingStatus;

}