package sideproject.gugumo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class FcmTokenDto {

    @NotEmpty
    private String fcmToken;
}
