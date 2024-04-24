package sideproject.gugumo.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sideproject.gugumo.domain.dto.SignUpMemberDto;
import sideproject.gugumo.domain.entity.Member;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/v1/member")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> saveMember(@RequestBody @Valid SignUpMemberDto signUpMemberDto) {

        Member joinMember = Member.createUserBuilder()
                .email(signUpMemberDto.getEmail())
                .nickname(signUpMemberDto.getNickname())
                .password(passwordEncoder.encode(signUpMemberDto.getPassword()))
                .build();

        Long joinId = memberService.join(joinMember);

        return ApiResponse.createSuccess(joinId);
    }
}
