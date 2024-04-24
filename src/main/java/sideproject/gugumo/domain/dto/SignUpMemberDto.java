package sideproject.gugumo.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class SignUpMemberDto {
    @NotBlank
    private String nickname;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
