package sideproject.gugumo.domain.dto.simplepostdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@AllArgsConstructor
public class SimpleTransShortDto extends SimpleTransPostDto{

    private LocalDateTime meetingDateTime;
}
