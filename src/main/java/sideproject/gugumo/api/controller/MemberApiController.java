package sideproject.gugumo.api.controller;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.MemberMapper;
import sideproject.gugumo.domain.dto.SignUpMemberDto;
import sideproject.gugumo.domain.entity.Member;
import sideproject.gugumo.domain.entity.MemberStatus;
import sideproject.gugumo.exception.DuplicateEmailException;
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

        signUpMemberDto.setPassword(passwordEncoder.encode(signUpMemberDto.getPassword()));
        Member joinMember = Mappers.getMapper(MemberMapper.class).toEntity(signUpMemberDto, MemberStatus.active);

        Long joinId = memberService.join(joinMember);
        return ApiResponse.createSuccess(joinId);
    }


    @Data
    static class CreateMemberResponse {
        Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
