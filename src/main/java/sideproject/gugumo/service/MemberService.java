package sideproject.gugumo.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.dto.memberDto.MemberDto;
import sideproject.gugumo.domain.dto.memberDto.MemberInfoDto;
import sideproject.gugumo.domain.dto.memberDto.SignUpMemberDto;
import sideproject.gugumo.domain.dto.memberDto.UpdateMemberDto;
import sideproject.gugumo.domain.entity.member.FavoriteSport;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.member.MemberRole;
import sideproject.gugumo.domain.entity.member.MemberStatus;
import sideproject.gugumo.exception.exception.DuplicateEmailException;
import sideproject.gugumo.exception.exception.DuplicateNicknameException;
import sideproject.gugumo.exception.exception.NoAuthorizationException;
import sideproject.gugumo.exception.exception.UserNotFoundException;
import sideproject.gugumo.repository.FavoriteSportRepository;
import sideproject.gugumo.repository.MemberRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FavoriteSportRepository favoriteSportRepository;

    @Transactional
    public Long join(SignUpMemberDto signUpMemberDto) {

        String encodePassword = passwordEncoder.encode(signUpMemberDto.getPassword());

        Member joinMember = Member.userJoin()
                .username(signUpMemberDto.getUsername())
                .nickname(signUpMemberDto.getNickname())
                .password(encodePassword)
                .isAgreeMarketing(signUpMemberDto.isAgreeMarketing())
                .isAgreeCollectingUsingPersonalInformation(signUpMemberDto.isAgreeCollectingUsingPersonalInformation())
                .isAgreeTermsUse(signUpMemberDto.isAgreeTermsUse())
                .build();

        validateDuplicateMemberByUsername(joinMember);
        validateDuplicateMemberByNickname(joinMember);

        String favoriteSports = signUpMemberDto.getFavoriteSports();

        if(favoriteSports != null) {
            String[] split = favoriteSports.split(",");
            for(String str : split) {
                FavoriteSport favoriteSport = FavoriteSport.createFavoriteSport(str, joinMember);
                favoriteSportRepository.save(favoriteSport);
            }
        }
        
        memberRepository.save(joinMember);

        return joinMember.getId();
    }

    /**
     * deprecated
     * @param memberId
     * @return
     */
    public MemberDto findOne(Long memberId) {

        Member findMember = memberRepository.findOne(memberId);

        if (findMember == null) {
            throw new UserNotFoundException("회원이 없습니다.");
        }

        return MemberDto.builder()
                .id(findMember.getId())
                .username(findMember.getUsername())
                .nickname(findMember.getNickname())
                .role(findMember.getRole())
                .status(findMember.getStatus())
                .profileImagePath(findMember.getProfileImagePath())
                .build();
    }

    public MemberInfoDto getMemberInfo(Long id, String username) {
        Member findMember = memberRepository.findOne(id);

        if (findMember == null) {
            throw new UserNotFoundException("회원이 없습니다.");
        }

        if(!Objects.equals(findMember.getUsername(), username)) {
            throw new NoAuthorizationException("권한이 없습니다.");
        }

        List<FavoriteSport> favoriteSportList = favoriteSportRepository.getFavoriteSports(findMember);

        StringBuilder favoriteSports = new StringBuilder();

        for (FavoriteSport fs : favoriteSportList) {
            favoriteSports.append(fs.getGameType().name());
            favoriteSports.append(',');
        }
        favoriteSports.deleteCharAt(favoriteSports.length()-1);

        return MemberInfoDto.builder()
                .username(findMember.getUsername())
                .nickname(findMember.getNickname())
                .favoriteSports(favoriteSports.toString())
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

    public MemberDto findByNickname(String nickname) {
        Optional<Member> findMember = memberRepository.findByNickname(nickname);

        return findMember.map(member->MemberDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .profileImagePath(member.getProfileImagePath())
                .status(member.getStatus())
                .role(member.getRole())
                .id(member.getId())
                .build()).orElse(null);
    }

    public Boolean isExistNickname(String nickname) {
        Optional<Member> byNickname = memberRepository.findByNickname(nickname);

        return byNickname.isPresent();
    }

    @Transactional
    public void update(Long id, UpdateMemberDto updateMemberDto) {
        Member findMember = memberRepository.findOne(id);

        updateMemberDto.setPassword(passwordEncoder.encode(updateMemberDto.getPassword()));

        findMember.updateMember(updateMemberDto);
    }

    @Transactional
    public void updateNickname(Long id, String nickname) {
        Member findMember = memberRepository.findOne(id);

        Optional<Member> byNickname = memberRepository.findByNickname(nickname);

        if(byNickname.isPresent()) {
            throw new DuplicateNicknameException("이미 존재하는 닉네임입니다.");
        }

        findMember.updateMemberNickname(nickname);
    }

    private void validateDuplicateMemberByUsername(Member member) {
        Optional<Member> findMember = memberRepository.findByUsername(member.getUsername());

        if(findMember.isPresent()) {
            throw new DuplicateEmailException("이미 존재하는 회원입니다.");
        }
    }

    private void validateDuplicateMemberByNickname(Member member) {
        Optional<Member> findMember = memberRepository.findByNickname(member.getNickname());

        if(findMember.isPresent()) {
            throw new DuplicateNicknameException("이미 존재하는 닉네임입니다.");
        }
    }

    @Transactional
    public void updatePassword(Long id, String password) {

        Member findMember = memberRepository.findOne(id);

        findMember.updateMemberPassword(passwordEncoder.encode(password));
    }

    @Transactional
    public String resetPassword(String username) {
        Optional<Member> findMember = memberRepository.findByUsername(username);

        String newPassword = RandomStringUtils.randomAlphanumeric(10);

        if(findMember.isEmpty()) {
            throw new UserNotFoundException("회원이 없습니다.");
        }

        findMember.get().updateMemberPassword(passwordEncoder.encode(newPassword));

        return newPassword;
    }

    @Transactional
    public void deleteMember(String username) {

        Optional<Member> findMember = memberRepository.findByUsername(username);

        if(findMember.isEmpty()) {
            throw new UserNotFoundException("존재하지 않는 회원입니다.");
        }

        findMember.get().deleteMember();

    }
}
