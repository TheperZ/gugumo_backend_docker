package sideproject.gugumo.domain.dto.memberDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sideproject.gugumo.domain.entity.member.MemberRole;
import sideproject.gugumo.domain.entity.member.MemberStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoDto {

    private String username;
    private String nickname;
    private String favoriteSports;

    @Builder
    public MemberInfoDto(String username, String nickname, String favoriteSports) {
        this.username = username;
        this.nickname = nickname;
        this.favoriteSports = favoriteSports;
    }
}
