package sideproject.gugumo.domain.dto.memberDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberInfoDto {
    private String password;
    private String nickname;
    private String profileImagePath;

    @Builder
    public UpdateMemberInfoDto(String password, String nickname, String profileImagePath) {
        this.password = password;
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
    }
}
