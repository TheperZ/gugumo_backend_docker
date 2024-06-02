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
    private String favoriteSports;
    private String emailAuth;
}
