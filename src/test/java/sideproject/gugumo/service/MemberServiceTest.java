package sideproject.gugumo.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.dto.memberDto.SignUpMemberDto;
import sideproject.gugumo.exception.exception.DuplicateEmailException;
import sideproject.gugumo.exception.exception.DuplicateNicknameException;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("nickname을 업데이트 할 때 중복된 nickname일 경우 DuplicateNicknameException 예외가 발생한다..")
    public void updateNicknameDuplicateTest() {

        //given
        SignUpMemberDto signUpMemberDto1 = SignUpMemberDto.builder()
                .nickname("nickname123")
                .password("password123")
                .username("username123")
                .build();

        String duplicateNickname = "nickname123";

        Long id = memberService.join(signUpMemberDto1);

//        memberService.updateNickname(id, duplicateNickname);

        //than
        Assertions.assertThatThrownBy(()->memberService.updateNickname(id, duplicateNickname)).isInstanceOf(DuplicateNicknameException.class);
    }

    @Test
    @DisplayName("nickname을 업데이트 할 수 있다.")
    public void updateNicknameTest() {
        //given
        SignUpMemberDto signUpMemberDto1 = SignUpMemberDto.builder()
                .nickname("nickname123")
                .password("password123")
                .username("username123")
                .build();

        String changeNickname = "nickname1234";

        //when
        Long id = memberService.join(signUpMemberDto1);

        memberService.updateNickname(id, changeNickname);
//        MemberDto updateMember = memberService.findOne(id);

        //than
//        Assertions.assertThat("nickname1234").isEqualTo(updateMember.getNickname());
    }


    @Test
    @DisplayName("service를 통해 member를 저장할 수 있다.")
    public void memberJoinTest() {
        //given
        SignUpMemberDto signUpMemberDto1 = SignUpMemberDto.builder()
                .nickname("nickname123")
                .password("password123")
                .username("username123")
                .build();

        //when

        //than
        Assertions.assertThatCode(()->memberService.join(signUpMemberDto1)).doesNotThrowAnyException();
    }


    @Test
    @DisplayName("중복되는 email로 가입시 IllegalStateException 에러를 발생한다.")
    public void duplicateMemberJoinTest() {
        //given
        SignUpMemberDto signUpMemberDto1 = SignUpMemberDto.builder()
                .nickname("nickname123")
                .password("password123")
                .username("username123")
                .build();

        SignUpMemberDto signUpMemberDto2 = SignUpMemberDto.builder()
                .nickname("nickname123")
                .password("password123")
                .username("username123")
                .build();

        //when
        memberService.join(signUpMemberDto1);

        //than
        Assertions.assertThatThrownBy(()->memberService.join(signUpMemberDto2)).isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("Member Delete 테스트")
    public void memberDeleteTest() {
        //given
        SignUpMemberDto signUpMemberDto1 = SignUpMemberDto.builder()
                .nickname("nickname123")
                .password("password123")
                .username("username123")
                .build();

        Long id = memberService.join(signUpMemberDto1);

        //when
        memberService.deleteMember(id);

        //than
//        MemberDto findMember = memberService.findByUsername(signUpMemberDto1.getUsername());
//
//        Assertions.assertThat(findMember.getStatus()).isEqualTo(MemberStatus.delete);
    }

}