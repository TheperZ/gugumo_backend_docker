package sideproject.gugumo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;


@Getter
public class UpdatePostReq {


    @NotEmpty
    private String meetingType;
    @NotEmpty
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
    private String meetingStatus;

}