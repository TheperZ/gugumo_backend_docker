package sideproject.gugumo.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.memberDto.KakaoUserInfoResponseDto;
import sideproject.gugumo.domain.dto.memberDto.SignUpEmailMemberDto;
import sideproject.gugumo.domain.dto.memberDto.SignUpKakaoMemberDto;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.KakaoService;
import sideproject.gugumo.service.MemberService;

@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final MemberService memberService;

    @GetMapping("/kakao/login")
    public ApiResponse<String> login(@RequestParam(name = "code") String code) {

        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        Boolean isJoined = memberService.isJoinedKakaoMember(userInfo.getId());
        StringBuilder loginResult = new StringBuilder();

        if(isJoined) {
            loginResult.append("Bearer ").append(memberService.kakaoLogin(userInfo.getId()));
        }
        else {
            loginResult.append("not joined");
        }

//        return loginResult.toString();
        return ApiResponse.createSuccess(loginResult.toString());
    }

    @PostMapping("/api/v1/kakao/member")
    public ApiResponse<Boolean> join(@RequestBody SignUpKakaoMemberDto signUpKakaoMemberDto) {
        memberService.kakaoJoinMember(signUpKakaoMemberDto);

        return ApiResponse.createSuccess(true);
    }
}
