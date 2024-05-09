package sideproject.gugumo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CreatePostReq {


    @NotEmpty
    private String meetingType;     //단기, 장기
    @NotEmpty
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
