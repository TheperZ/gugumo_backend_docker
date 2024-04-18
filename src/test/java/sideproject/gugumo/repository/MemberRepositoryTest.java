package sideproject.gugumo.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.MemberStatus;
import sideproject.gugumo.service.MemberService;

import java.util.Optional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("DB에 저장시 에러가 없어야 한다.")
    public void memberRepositoryTest() {
        //given
        Member member = Member.builder()
                .email("email")
                .password("password")
                .nickname("nickname")
                .status(MemberStatus.active)
                .build();

        //when

        //than
        Assertions.assertThatCode(()->memberRepository.save(member)).doesNotThrowAnyException();

    }

    @Test
    @DisplayName("findOne 함수를 통해 Member를 찾을 수 있다.")
    public void findOneTest() {
        //given
        Member member = Member.builder()
                .email("email")
                .password("password")
                .nickname("nickname")
                .status(MemberStatus.active)
                .build();

        //when
        memberRepository.save(member);
        Member findMember = memberRepository.findOne(member.getId());

        //than
        Assertions.assertThat(findMember).isEqualTo(member);
    }


    @Test
    @DisplayName("findByEmail 함수를 통해 Member를 찾을 수 있다.")
    public void findMemberTest() {
        //given
        Member member = Member.builder()
                .email("email")
                .password("password")
                .nickname("nickname")
                .status(MemberStatus.active)
                .build();

        //when
        memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findByEmail("email");
        findMember.orElseThrow(()->new IllegalStateException("없는 회원입니다."));

        //than
        Assertions.assertThat(member).isEqualTo(findMember.get());
    }

}