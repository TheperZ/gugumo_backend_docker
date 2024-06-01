package sideproject.gugumo.domain.dto.emailDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class EmailRequestDto {
    @Email
    @NotEmpty
    private String email;
}
