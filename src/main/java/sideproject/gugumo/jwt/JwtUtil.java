package sideproject.gugumo.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private SecretKey getSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //TODO issuedAt, expiration에서 시간은 외부에서 주입하는 방식으로 변경하는 쪽이 테스트 하기 편할 것 같음.
    public String createJwt(String username, String secretKey, Long expiredMs) {

        return Jwts.builder()
                .issuer(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    public String parseIssuer(String token, String secretKey) {
        return Jwts.parser().verifyWith(getSigningKey(secretKey)).build().parseSignedClaims(token).getPayload().getIssuer();
    }
}
