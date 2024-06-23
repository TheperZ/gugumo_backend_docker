package sideproject.gugumo.domain.dto.memberDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpKakaoMemberDto {
    private String nickname;
    private String favoriteSports;
    private Long kakaoId;
    // 서비스 이용 약관 동의 여부
    boolean isAgreeTermsUse;
    // 개인정보 수집 및 이용 동의
    boolean isAgreeCollectingUsingPersonalInformation;
    // 마케팅 수신 동의
    boolean isAgreeMarketing;

    @Builder
    public SignUpKakaoMemberDto(String nickname, String favoriteSports, Long kakaoId, boolean isAgreeTermsUse, boolean isAgreeCollectingUsingPersonalInformation, boolean isAgreeMarketing) {
        this.nickname = nickname;
        this.favoriteSports = favoriteSports;
        this.kakaoId = kakaoId;
        this.isAgreeTermsUse = isAgreeTermsUse;
        this.isAgreeCollectingUsingPersonalInformation = isAgreeCollectingUsingPersonalInformation;
        this.isAgreeMarketing = isAgreeMarketing;
    }
}
