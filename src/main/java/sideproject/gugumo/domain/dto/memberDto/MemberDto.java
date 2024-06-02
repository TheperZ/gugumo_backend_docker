package sideproject.gugumo.domain.dto.memberDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sideproject.gugumo.domain.entity.member.MemberRole;
import sideproject.gugumo.domain.entity.member.MemberStatus;

/**
 * service 에서 Member를 반환할 때 사용할 dto
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private Long id;
    private String username;
    private String nickname;
    private String profileImagePath;
    MemberStatus status;
    MemberRole role;

    @Builder
    public MemberDto(Long id, String username, String nickname, String profileImagePath, MemberStatus status, MemberRole role) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
        this.status = status;
        this.role = role;
    }
}
