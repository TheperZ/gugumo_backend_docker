package sideproject.gugumo.domain.dto.simplepostdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Getter
@SuperBuilder
@AllArgsConstructor
public class SimpleTransLongDto extends SimpleTransPostDto{


    private LocalTime meetingTime;
    private String meetingDays;
}
