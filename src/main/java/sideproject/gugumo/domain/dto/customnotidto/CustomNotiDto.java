package sideproject.gugumo.domain.dto.customnotidto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sideproject.gugumo.domain.entity.NotificationType;

import java.time.LocalDateTime;


@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class CustomNotiDto {


    private Long id;
    private String content;
    private NotificationType notificationType;

    private LocalDateTime createDate;
    private boolean isRead;




}
