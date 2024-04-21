package sideproject.gugumo.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.entity.Member;
import sideproject.gugumo.domain.entity.MemberStatus;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("service를 통해 member를 저장할 수 있다.")
    public void memberJoinTest() {
        //given
        Member member = Member.builder()
                .email("email")
                .password("password")
                .nickname("nickname")
                .status(MemberStatus.active)
                .build();

        //when


        //than
        Assertions.assertThatCode(()->memberService.join(member)).doesNotThrowAnyException();
    }


    @Test
    @DisplayName("중복되는 email로 가입시 IllegalStateException 에러를 발생한다.")
    public void duplicateMemberJoinTest() {
        //given
        Member member1 = Member.builder()
                .email("email")
                .password("password")
                .nickname("nickname")
                .status(MemberStatus.active)
                .build();

        Member member2 = Member.builder()
                .email("email")
                .password("password")
                .nickname("nickname")
                .status(MemberStatus.active)
                .build();

        //when
        memberService.join(member1);

        //than
        Assertions.assertThatThrownBy(()->memberService.join(member2)).isInstanceOf(IllegalStateException.class);
    }
}