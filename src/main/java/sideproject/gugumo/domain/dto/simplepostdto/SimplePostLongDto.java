package sideproject.gugumo.domain.dto.simplepostdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Getter
@SuperBuilder
@AllArgsConstructor
public class SimplePostLongDto extends SimplePostDto {


    private LocalTime meetingTime;
    private String meetingDays;
}
