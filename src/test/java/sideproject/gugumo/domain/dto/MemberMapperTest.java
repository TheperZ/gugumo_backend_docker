package sideproject.gugumo.domain.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sideproject.gugumo.domain.entity.Member;
import sideproject.gugumo.domain.entity.MemberStatus;

@SpringBootTest
class MemberMapperTest {

    @Test
    @DisplayName("SignupMemberDto에서 Member로 변환이 가능해야 한다.")
    public void SignupToMemberTest() {
        //given
        SignUpMemberDto signUpMemberDto = new SignUpMemberDto();
        signUpMemberDto.setEmail("email");
        signUpMemberDto.setPassword("password");
        signUpMemberDto.setNickname("nickname");

        //when
        Member member = MemberMapper.INSTANCE.toEntity(signUpMemberDto, MemberStatus.active);
        System.out.println("member = " + member);

        //than

    }

}