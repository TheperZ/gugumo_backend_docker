package sideproject.gugumo.domain.dto.memberDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateMemberPasswordDto {
    String password;

    public UpdateMemberPasswordDto() {
    }

    @Builder
    public UpdateMemberPasswordDto(String password) {
        this.password = password;
    }
}
