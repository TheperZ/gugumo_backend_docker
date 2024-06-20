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

    // 삭제 예정
//    @PostMapping("/api/v1/member")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ApiResponse<Long> saveMember(@RequestBody @Valid SignUpMemberDto signUpMemberDto) {
//
//        Long joinId = memberService.join(signUpMemberDto);
//
//        return ApiResponse.createSuccess(joinId);
//    }

    @PostMapping("/api/v2/member")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> saveMemberWithEmailAuth(@RequestBody @Valid SignUpMemberDto signUpMemberDto) {

        mailService.checkAuthNum(signUpMemberDto.getUsername(), signUpMemberDto.getEmailAuthNum());

        Long joinId = memberService.join(signUpMemberDto);

        return ApiResponse.createSuccess(joinId);
    }

    @GetMapping("/api/v1/member")
    public ApiResponse<MemberInfoDto> getMemberInfo(@AuthenticationPrincipal CustomUserDetails principal) {

        String username = principal.getUsername();
        long id = principal.getId();

        MemberInfoDto memberInfoDto = memberService.getMemberInfo(id);

        return ApiResponse.createSuccess(memberInfoDto);
    }

      // 삭제 예정
//    @PatchMapping("/api/v1/member")
//    public ApiResponse<UpdateMemberInfoDto> updateMemberInfo(@AuthenticationPrincipal CustomUserDetails principal,
//                                                             @RequestBody UpdateMemberInfoDto updateMemberInfoDto) {
//
//        String username = principal.getUsername();
//        long id = principal.getId();
//
//        MemberDto findMemberDto = memberService.findOne(id);
//
//        if (findMemberDto == null) {
//            throw new UserNotFoundException(username + " 회원이 없습니다.");
//        }
//
//        if(!Objects.equals(findMemberDto.getUsername(), username)) {
//            throw new NoAuthorizationException("권한이 없습니다.");
//        }
//
//        //TODO update 시 비밀번호 체크 등 검증 필요
//        memberService.update(findMemberDto.getId(), updateMemberInfoDto);
//
//        // update를 진행한 후 update된 member를 다시 조회해서 확실하게 된것을 검증
//        MemberDto newMemberDto = memberService.findOne(findMemberDto.getId());
//
//        UpdateMemberInfoDto newUpdateMemberInfoDto = UpdateMemberInfoDto.builder()
//                .profileImagePath(newMemberDto.getProfileImagePath())
//                .nickname(newMemberDto.getNickname())
//                .password("")
//                .build();
//
//        return ApiResponse.createSuccess(newUpdateMemberInfoDto);
//    }

    @PatchMapping("/api/v1/member/updateNickname")
    public ApiResponse<MemberInfoDto> updateMemberNickname(@AuthenticationPrincipal CustomUserDetails principal,
                                                   @RequestBody UpdateMemberNicknameDto updateMemberNicknameDto) {

        String updateNickname = updateMemberNicknameDto.getNickname();
        long id = principal.getId();

        // 회원 정보 수정
        memberService.updateNickname(id, updateNickname);

        // 수정한 정보로 회원 조회
        MemberInfoDto memberInfo = memberService.getMemberInfo(id);

        return ApiResponse.createSuccess(memberInfo);
    }

    @GetMapping("/api/v1/member/checkDuplicateNickname")
    public ApiResponse<Boolean> checkDuplicateNickname(@RequestParam String nickname) {

        Boolean isExistNickname = memberService.isExistNickname(nickname);

        return ApiResponse.createSuccess(isExistNickname);
    }

    @PatchMapping("/api/v1/member/updatePassword")
    public ApiResponse<Boolean> updateMemberPassword(@AuthenticationPrincipal CustomUserDetails principal,
                                                    @RequestBody UpdateMemberPasswordDto updateMemberPasswordDto) {

        long id = principal.getId();

        memberService.updatePassword(id, updateMemberPasswordDto.getPassword());

        return ApiResponse.createSuccess(true);
    }

    @DeleteMapping("/api/v1/member")
    public ApiResponse<Boolean> deleteMember(@AuthenticationPrincipal CustomUserDetails principal) {

        long id = principal.getId();

        memberService.deleteMember(id);

        return ApiResponse.createSuccess(true);
    }

    @PostMapping("/api/v1/resetPassword")
    public ApiResponse<Boolean> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {

        String username = resetPasswordDto.getEmail();

        String newPassword = memberService.resetPassword(username);

        mailService.resetPassword(username, newPassword);

        return ApiResponse.createSuccess(true);
    }
}
