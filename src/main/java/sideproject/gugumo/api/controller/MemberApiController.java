package sideproject.gugumo.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.CustomUserDetails;
import sideproject.gugumo.domain.dto.MemberDto;
import sideproject.gugumo.domain.dto.SignUpMemberDto;
import sideproject.gugumo.domain.dto.UpdateMemberDto;
import sideproject.gugumo.domain.entity.Member;
import sideproject.gugumo.exception.DuplicateEmailException;
import sideproject.gugumo.exception.NoAuthorizationException;
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

    @GetMapping("/api/v1/member/{id}")
    public ApiResponse<UpdateMemberDto> getMember(@AuthenticationPrincipal CustomUserDetails principal,
                                                  @PathVariable("id") Long id) {

        MemberDto findMemberDto = memberService.findOne(id);

        if(!Objects.equals(findMemberDto.getUsername(), principal.getUsername())) {
            throw new NoAuthorizationException("권한이 없습니다.");
        }

        UpdateMemberDto updateMemberDto = UpdateMemberDto.builder()
                .nickname(findMemberDto.getNickname())
                .profileImagePath(findMemberDto.getProfileImagePath())
                .password("")
                .build();

        return ApiResponse.createSuccess(updateMemberDto);
    }

    @PatchMapping("/api/v1/member/{id}")
    public ApiResponse<UpdateMemberDto> editMember(@AuthenticationPrincipal CustomUserDetails principal,
                           @PathVariable("id") Long id,
                           @RequestBody UpdateMemberDto updateMemberDto) {

        MemberDto findMemberDto = memberService.findOne(id);

        if(!Objects.equals(findMemberDto.getUsername(), principal.getUsername())) {
            throw new NoAuthorizationException("권한이 없습니다.");
        }

        //TODO update 하면서 예외가 발생하는 경우 고려 안해봄. 있을 경우 추가해야함.(비밀번호가 null이라던가 공백이라던가 등)
        memberService.update(id, updateMemberDto);

        MemberDto newMemberDto = memberService.findOne(id);

        UpdateMemberDto newUpdateMemberDto = UpdateMemberDto.builder()
                .profileImagePath(newMemberDto.getProfileImagePath())
                .nickname(newMemberDto.getNickname())
                .password("")
                .build();

        return ApiResponse.createSuccess(newUpdateMemberDto);
    }

    /**
     * 권한별 접근 test 용도로 임시 작성
     * /user, /admin
     * @return
     */
    @GetMapping("/user")
    public String userP() {
        return "User Controller";
    }

    @GetMapping("/admin")
    public String adminP() {
        return "User Controller";
    }
}
