package sideproject.gugumo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import sideproject.gugumo.domain.meeting.GameType;
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
public class CreatePostReq {


    @NotEmpty
    @EnumValue(enumClass = MeetingType.class)
    private String meetingType;     //단기, 장기
    @NotEmpty
    @EnumValue(enumClass = GameType.class)
    private String gameType;
    @NotNull
    private int meetingMemberNum;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate meetingDate;      //날짜: 단기일 경우
    private String meetingDays;     //요일: 장기일 경우
    @NotNull
    private int meetingTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
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

}
