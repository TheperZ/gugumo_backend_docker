package sideproject.gugumo.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

class JwtUtilTest {

    String secretKey = "owiefhjioEIOLKSDFjiowefjoiJFSILWEIOFJHLKSDiodsfjEIOEklsdfioEFH";
    Long expiredMs = 86400000L;

    @Test
    @DisplayName("jwt token 생성")
    public void createJwtToken() {
        //given

        JwtUtil jwtUtil = new JwtUtil();
        String jwtToken = jwtUtil.createJwt("heeseong", secretKey, expiredMs);

        //when


        //than
        System.out.println("jwtToken = " + jwtToken);
        Assertions.assertThat(jwtUtil.parseIssuer(jwtToken, secretKey)).isEqualTo("heeseong");
    }
}