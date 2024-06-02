package sideproject.gugumo.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.memberDto.*;
import sideproject.gugumo.exception.exception.NoAuthorizationException;
import sideproject.gugumo.exception.exception.UserNotFoundException;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.MailSenderService;
import sideproject.gugumo.service.MemberService;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final MailSenderService mailService;

    @PostMapping("/api/v1/member")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> saveMember(@RequestBody @Valid SignUpMemberDto signUpMemberDto) {

        Long joinId = memberService.join(signUpMemberDto);

        return ApiResponse.createSuccess(joinId);
    }

    @PostMapping("/api/v2/member")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> saveMemberWithEmailAuth(@RequestBody @Valid SignUpMemberDto signUpMemberDto) {

        boolean checked = mailService.checkAuthNum(signUpMemberDto.getUsername(), signUpMemberDto.getEmailAuthNum());

        if(!checked) {
            throw new NoAuthorizationException("이메일 인증 에러");
        }

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

        if (findMemberDto == null) {
            throw new UserNotFoundException(username + " 회원이 없습니다.");
        }

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

    @PatchMapping("/api/v1/member/updateNickname")
    public ApiResponse<String> updateMemberNickname(@AuthenticationPrincipal CustomUserDetails principal,
                                                   @RequestBody UpdateMemberNicknameDto updateMemberNicknameDto) {

        String username = principal.getUsername();

        MemberDto member = memberService.findByUsername(username);

        memberService.updateNickname(member.getId(), updateMemberNicknameDto.getNickname());

        MemberDto updateMember = memberService.findOne(member.getId());

        String updateMemberNickname = updateMember.getNickname();

        return ApiResponse.createSuccess(updateMemberNickname);
    }

    @GetMapping("/api/v1/member/checkDuplicateNickname")
    public ApiResponse<Boolean> checkDuplicateNickname(/*@AuthenticationPrincipal CustomUserDetails principal,*/
                                                      /*@RequestBody UpdateMemberNicknameDto updateMemberNicknameDto,*/
                                                      @RequestParam String nickname) {

//        Boolean isExistNickname = memberService.isExistNickname(updateMemberNicknameDto.getNickname());
        Boolean isExistNickname = memberService.isExistNickname(nickname);

        return ApiResponse.createSuccess(isExistNickname);
    }

    @PatchMapping("/api/v1/member/updatePassword")
    public ApiResponse<Boolean> updateMemberPassword(@AuthenticationPrincipal CustomUserDetails principal,
                                                    @RequestBody UpdateMemberPasswordDto updateMemberPasswordDto) {

        String username = principal.getUsername();

        MemberDto member = memberService.findByUsername(username);

        memberService.updatePassword(member.getId(), updateMemberPasswordDto.getPassword());

        return ApiResponse.createSuccess(true);
    }

    @DeleteMapping("/api/v1/member")
    public ApiResponse<Boolean> deleteMember(@AuthenticationPrincipal CustomUserDetails principal) {

        String username = principal.getUsername();

        memberService.deleteMember(username);

        return ApiResponse.createSuccess(true);
    }
}
