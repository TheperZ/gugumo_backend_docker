package sideproject.gugumo.domain.dto.memberDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoLoginRequestDto {
    private String username;
    private String nickname;
    private Long kakaoId;
    private String profilePath;

    @Builder
    public KakaoLoginRequestDto(String username, String nickname, Long kakaoId, String profilePath) {
        this.username = username;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
        this.profilePath = profilePath;
    }
}
