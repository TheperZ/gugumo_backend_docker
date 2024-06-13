package sideproject.gugumo.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideproject.gugumo.domain.dto.memberDto.KakaoUserInfoResponseDto;
import sideproject.gugumo.service.KakaoService;

@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/kakao/callback")
    public String login(@RequestParam(name = "code") String code) {

        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        return accessToken;
    }
}
