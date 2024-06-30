package sideproject.gugumo.domain.dto.memberDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponseDto {
    //회원 번호
    @JsonProperty("id")
    private Long id;

    //서비스에 연결 완료된 시각. UTC
    @JsonProperty("connected_at")
    private Date connectedAt;

    //사용자 프로퍼티
    @JsonProperty("properties")
    private HashMap<String, String> properties;

    //카카오 계정 정보
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAccount {
        //닉네임 제공 동의 여부
        @JsonProperty("profile_nickname_needs_agreement")
        private Boolean isNickNameAgree;

        //프로필 사진 제공 동의 여부
        @JsonProperty("profile_image_needs_agreement")
        private Boolean isProfileImageAgree;

        //사용자 프로필 정보
        @JsonProperty("profile")
        private Profile profile;

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Profile {

            //닉네임
            @JsonProperty("nickname")
            private String nickName;

            //프로필 미리보기 이미지 URL
            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;

            //프로필 사진 URL
            @JsonProperty("profile_image_url")
            private String profileImageUrl;

            //프로필 사진 URL 기본 프로필인지 여부
            //true : 기본 프로필, false : 사용자 등록
            @JsonProperty("is_default_image")
            private String isDefaultImage;

            //닉네임이 기본 닉네임인지 여부
            //true : 기본 닉네임, false : 사용자 등록
            @JsonProperty("is_default_nickname")
            private Boolean isDefaultNickName;

        }
    }
}
