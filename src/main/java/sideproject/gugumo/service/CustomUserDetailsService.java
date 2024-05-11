package sideproject.gugumo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sideproject.gugumo.domain.entity.Member;
import sideproject.gugumo.domain.dto.CustomUserDetails;
import sideproject.gugumo.repository.MemberRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> findMember = memberRepository.findByUsername(username);

        /*
        if (findMember.isPresent()) {
            return new CustomUserDetails(findMember.get());
        }

        return null;
         */
        return findMember.map(CustomUserDetails::new).orElse(null);
    }
}
