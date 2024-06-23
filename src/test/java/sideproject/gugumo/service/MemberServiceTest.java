package sideproject.gugumo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.entity.member.Member;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    Member getJoinMemberBuild() {
        return Member.userJoin()
                .username("donald010@naver.com")
                .password("password")
                .nickname("nickname")
                .isAgreeMarketing(true)
                .isAgreeTermsUse(true)
                .isAgreeCollectingUsingPersonalInformation(true)
                .build();
    }

    @Test
    @DisplayName("")
    public void joinMemberTest() {
        //given

                
        //when
        
        
        //than
        
    }
        

}