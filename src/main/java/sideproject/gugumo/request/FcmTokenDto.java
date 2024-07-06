package sideproject.gugumo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FcmTokenDto {

    private String FCMtoken;
}
