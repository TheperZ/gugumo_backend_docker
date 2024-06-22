package sideproject.gugumo.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.dto.memberDto.EmailLoginCreateJwtDto;
import sideproject.gugumo.domain.dto.memberDto.EmailLoginRequestDto;
import sideproject.gugumo.domain.dto.memberDto.MemberInfoDto;
import sideproject.gugumo.domain.dto.memberDto.SignUpMemberDto;
import sideproject.gugumo.domain.entity.member.FavoriteSport;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.member.MemberStatus;
import sideproject.gugumo.exception.exception.DuplicateEmailException;
import sideproject.gugumo.exception.exception.DuplicateNicknameException;
import sideproject.gugumo.exception.exception.UserNotFoundException;
import sideproject.gugumo.jwt.JwtUtil;
import sideproject.gugumo.repository.FavoriteSportRepository;
import sideproject.gugumo.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FavoriteSportRepository favoriteSportRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Long joinMember(SignUpMemberDto signUpMemberDto) {

        String encodePassword = passwordEncoder.encode(signUpMemberDto.getPassword());

        Member joinMember = Member.userJoin()
                .username(signUpMemberDto.getUsername())
                .nickname(signUpMemberDto.getNickname())
                .password(encodePassword)
                .isAgreeMarketing(signUpMemberDto.isAgreeMarketing())
                .isAgreeCollectingUsingPersonalInformation(signUpMemberDto.isAgreeCollectingUsingPersonalInformation())
                .isAgreeTermsUse(signUpMemberDto.isAgreeTermsUse())
                .build();

        validateDuplicateMemberByUsername(joinMember.getUsername());
        validateDuplicateMemberByNickname(joinMember.getNickname());

        String favoriteSports = signUpMemberDto.getFavoriteSports();

        if(!favoriteSports.isEmpty()) {
            String[] split = favoriteSports.split(",");
            for(String str : split) {
                FavoriteSport favoriteSport = FavoriteSport.createFavoriteSport(str, joinMember);
                favoriteSportRepository.save(favoriteSport);
            }
        }
        
        memberRepository.save(joinMember);

        return joinMember.getId();
    }


    public MemberInfoDto getMemberInfo(Long id) {

        Member findMember = memberRepository.findOne(id)
                .orElseThrow(()->new UserNotFoundException("회원이 없습니다."));

        List<FavoriteSport> favoriteSportList = favoriteSportRepository.getFavoriteSports(findMember);

        StringBuilder favoriteSports = new StringBuilder();

        if(!favoriteSportList.isEmpty()) {
            for (FavoriteSport fs : favoriteSportList) {
                favoriteSports.append(fs.getGameType().name());
                favoriteSports.append(',');
            }
            favoriteSports.deleteCharAt(favoriteSports.length()-1);
        }

        return MemberInfoDto.builder()
                .username(findMember.getUsername())
                .nickname(findMember.getNickname())
                .favoriteSports(favoriteSports.toString())
                .build();

    }

    // 삭제 예정
//    public MemberInfoDto findByUsername(String username) {
//
//        Member findMember = memberRepository.findByUsername(username)
//                .orElseThrow(() -> new UserNotFoundException("회원이 없습니다."));
//
//        return getMemberInfo(findMember.getId());
//    }


    // 삭제 예정
//    public MemberInfoDto findByNickname(String nickname) {
//        Member findMember = memberRepository.findByNickname(nickname)
//                .orElseThrow(() -> new UserNotFoundException("회원이 없습니다."));
//
//        return getMemberInfo(findMember.getId());
//    }

    public Boolean isExistNickname(String nickname) {
        Optional<Member> byNickname = memberRepository.findByNickname(nickname);

        return byNickname.isPresent();
    }

    @Transactional
    public void updateNickname(Long id, String nickname) {

        Member findMember = memberRepository.findOne(id)
                .orElseThrow(() -> new UserNotFoundException("회원이 없습니다."));

        validateDuplicateMemberByNickname(nickname);

        findMember.updateMemberNickname(nickname);
    }

    private void validateDuplicateMemberByUsername(String username) {
        Optional<Member> findMember = memberRepository.findByUsername(username);

        if(findMember.isPresent()) {
            throw new DuplicateEmailException("이미 존재하는 회원입니다.");
        }
    }

    private void validateDuplicateMemberByNickname(String nickname) {
        Optional<Member> findMember = memberRepository.findByNickname(nickname);

        if(findMember.isPresent()) {
            throw new DuplicateNicknameException("이미 존재하는 닉네임입니다.");
        }
    }

    @Transactional
    public void updatePassword(Long id, String password) {

        memberRepository.findOne(id)
                .ifPresent(member -> member.updateMemberPassword(passwordEncoder.encode(password)));
    }

    @Transactional
    public String resetPassword(String username) {
        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("회원이 없습니다."));

        String newPassword = RandomStringUtils.randomAlphanumeric(10);

        findMember.updateMemberPassword(passwordEncoder.encode(newPassword));

        return newPassword;
    }

    @Transactional
    public void deleteMember(Long id) {

        Member findMember = memberRepository.findOne(id)
                .orElseThrow(() -> new UserNotFoundException("회원이 없습니다."));

        if(findMember.getStatus() == MemberStatus.delete) {
            throw new IllegalStateException("이미 탈퇴한 회원입니다.");
        }

        findMember.deleteMember();
    }

    public String emailLogin(EmailLoginRequestDto emailLoginRequestDto) {

        Member findMember = memberRepository.findByUsername(emailLoginRequestDto.getUsername())
                .orElseThrow(() ->
                        new UserNotFoundException("회원이 없습니다.")
                );

        if(!passwordEncoder.matches(emailLoginRequestDto.getPassword(), findMember.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다.");
        }

        EmailLoginCreateJwtDto emailLoginDto = EmailLoginCreateJwtDto.builder()
                .id(findMember.getId())
                .username(findMember.getUsername())
                .role(findMember.getRole().name())
                .requestTimeMs(System.currentTimeMillis())
                .build();

        return jwtUtil.createJwt(emailLoginDto);
    }
}
