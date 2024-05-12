package sideproject.gugumo.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.CustomUserDetails;
import sideproject.gugumo.domain.dto.MemberDto;
import sideproject.gugumo.domain.dto.SignUpMemberDto;
import sideproject.gugumo.domain.dto.UpdateMemberDto;
import sideproject.gugumo.exception.exception.NoAuthorizationException;
import sideproject.gugumo.exception.exception.UserNotFoundException;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.MemberService;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/member")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> saveMember(@RequestBody @Valid SignUpMemberDto signUpMemberDto) {

        Long joinId = memberService.join(signUpMemberDto);

        return ApiResponse.createSuccess(joinId);
    }

    @GetMapping("/api/v1/member")
    public ApiResponse<UpdateMemberDto> getMember(@AuthenticationPrincipal CustomUserDetails principal) {

        String username = principal.getUsername();

        MemberDto findMemberDto = memberService.findByUsername(username);

        if(findMemberDto == null) {
            throw new UserNotFoundException(username + " 회원이 없습니다.");
        }

        if(!Objects.equals(findMemberDto.getUsername(), username)) {
            throw new NoAuthorizationException("권한이 없습니다.");
        }

        UpdateMemberDto updateMemberDto = UpdateMemberDto.builder()
                .nickname(findMemberDto.getNickname())
                .profileImagePath(findMemberDto.getProfileImagePath())
                .password("")
                .build();

        return ApiResponse.createSuccess(updateMemberDto);
    }

    @PatchMapping("/api/v1/member")
    public ApiResponse<UpdateMemberDto> editMember(@AuthenticationPrincipal CustomUserDetails principal,
                           @RequestBody UpdateMemberDto updateMemberDto) {

        String username = principal.getUsername();

        MemberDto findMemberDto = memberService.findByUsername(username);

        if(!Objects.equals(findMemberDto.getUsername(), username)) {
            throw new NoAuthorizationException("권한이 없습니다.");
        }

        //TODO update 시 비밀번호 체크 등 검증 필요
        memberService.update(findMemberDto.getId(), updateMemberDto);

        // update를 진행한 후 update된 member를 다시 조회해서 확실하게 된것을 검증
        MemberDto newMemberDto = memberService.findOne(findMemberDto.getId());

        UpdateMemberDto newUpdateMemberDto = UpdateMemberDto.builder()
                .profileImagePath(newMemberDto.getProfileImagePath())
                .nickname(newMemberDto.getNickname())
                .password("")
                .build();

        return ApiResponse.createSuccess(newUpdateMemberDto);
    }
}
