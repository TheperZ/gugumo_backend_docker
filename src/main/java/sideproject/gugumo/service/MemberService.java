package sideproject.gugumo.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.dto.MemberDto;
import sideproject.gugumo.domain.dto.SignUpMemberDto;
import sideproject.gugumo.domain.dto.UpdateMemberDto;
import sideproject.gugumo.domain.entity.Member;
import sideproject.gugumo.exception.DuplicateEmailException;
import sideproject.gugumo.repository.MemberRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(SignUpMemberDto signUpMemberDto) {

        Member joinMember = Member.createUserBuilder()
                .username(signUpMemberDto.getUsername())
                .nickname(signUpMemberDto.getNickname())
                .password(passwordEncoder.encode(signUpMemberDto.getPassword()))
                .build();

        validateDuplicateMember(joinMember);
        memberRepository.save(joinMember);

        return joinMember.getId();
    }

    public MemberDto findOne(Long memberId) {

        Member findMember = memberRepository.findOne(memberId);

        return MemberDto.builder()
                .id(findMember.getId())
                .username(findMember.getUsername())
                .nickname(findMember.getNickname())
                .role(findMember.getRole())
                .status(findMember.getStatus())
                .profileImagePath(findMember.getProfileImagePath())
                .build();
    }

    public MemberDto findByUsername(String username) {

        Optional<Member> findMember = memberRepository.findByUsername(username);

        return findMember.map(member -> MemberDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .profileImagePath(member.getProfileImagePath())
                .status(member.getStatus())
                .role(member.getRole())
                .id(member.getId())
                .build()).orElse(null);
    }

    @Transactional
    public void update(Long id, UpdateMemberDto updateMemberDto) {
        Member findMember = memberRepository.findOne(id);

        updateMemberDto.setPassword(passwordEncoder.encode(updateMemberDto.getPassword()));

        findMember.updateMember(updateMemberDto);
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = memberRepository.findByUsername(member.getUsername());

        if(findMember.isPresent()) {
            throw new DuplicateEmailException("이미 존재하는 회원입니다.");
        }
    }
}
