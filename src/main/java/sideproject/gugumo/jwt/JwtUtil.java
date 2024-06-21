package sideproject.gugumo.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sideproject.gugumo.domain.dto.memberDto.EmailLoginCreateJwtDto;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // JWT에서는 String 키를 사용하는 방식에서 SecretKey라는 객체를 키로 사용하는 방식으로 변경됨.
    private final SecretKey secretKey;
    private Long expiredMs;

//    /// 삭제 예정
//    @Getter
//    @Value("${spring.jwt.expiration_time}")
//    private String expiredMillis;
//    ///

    public JwtUtil(@Value("${spring.jwt.secret}") String secret, @Value("${spring.jwt.expiration_time}") String expiredMs) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.expiredMs = Long.parseLong(expiredMs);
    }

    public Long getId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", Long.class);
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

//    //TODO 시간은 외부에서 주입받도록 수정할 예정
//    // deprecated
//    public String createJwt(long id, String username, String role, Long expiredMs) {
//        return Jwts.builder()
//                .claim("id", id)
//                .claim("username", username)
//                .claim("role", role)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + expiredMs))
//                .signWith(secretKey)
//                .compact();
//    }

    public String createJwt(EmailLoginCreateJwtDto emailLoginCreateJwtDto) {

        Date requestDate = new Date(emailLoginCreateJwtDto.getRequestTimeMs());
        Date expireDate = new Date(emailLoginCreateJwtDto.getRequestTimeMs() + expiredMs);

        return Jwts.builder()
                .claim("id", emailLoginCreateJwtDto.getId())
                .claim("username", emailLoginCreateJwtDto.getUsername())
                .claim("role", emailLoginCreateJwtDto.getRole())
                .issuedAt(requestDate)
                .expiration(expireDate)
                .signWith(secretKey)
                .compact();
    }
}
