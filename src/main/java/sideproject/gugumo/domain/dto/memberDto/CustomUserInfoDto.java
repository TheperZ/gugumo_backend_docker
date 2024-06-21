package sideproject.gugumo.domain.dto.memberDto;

import lombok.Builder;
import lombok.Getter;
import sideproject.gugumo.domain.entity.member.MemberRole;

@Getter
public class CustomUserInfoDto {
    private String username;
    private Long id;
    private MemberRole role;
    private String password;

    @Builder
    public CustomUserInfoDto(String username, Long id, MemberRole role, String password) {
        this.username = username;
        this.id = id;
        this.role = role;
        this.password = password;
    }
}
