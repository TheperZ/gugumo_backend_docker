package sideproject.gugumo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.member.MemberStatus;
import sideproject.gugumo.exception.exception.UserNotFoundException;
import sideproject.gugumo.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // email 로그인만 여기를 통과하기 때문에 findByUsername 사용해도 될듯
        Optional<Member> findMember = memberRepository.findByUsername(username);

        if(findMember.isEmpty() || findMember.get().getStatus() == MemberStatus.delete) {
            throw new UserNotFoundException("회원이 없습니다.");
        }

        return new CustomUserDetails(findMember.get());
    }
}
