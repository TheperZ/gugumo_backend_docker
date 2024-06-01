package sideproject.gugumo.domain.dto.memberDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpMemberDto {
    private String nickname;
    private String username;
    private String password;

    @Builder
    public SignUpMemberDto(String nickname, String username, String password) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
    }
}
