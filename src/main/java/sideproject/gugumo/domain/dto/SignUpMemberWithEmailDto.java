package sideproject.gugumo.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpMemberWithEmailDto {
    private String nickname;
    private String username;
    private String password;
    private String emailAuth;

    @Builder
    public SignUpMemberWithEmailDto(String nickname, String username, String password, String emailAuth) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.emailAuth = emailAuth;
    }
}
