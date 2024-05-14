package sideproject.gugumo.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateMemberNicknameDto {

    private String nickname;

    public UpdateMemberNicknameDto() {
    }

    @Builder
    public UpdateMemberNicknameDto(String nickname) {
        this.nickname = nickname;
    }
}
