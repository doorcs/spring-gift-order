package gift.util;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import gift.domain.Member;
import io.jsonwebtoken.Jwts;

@Component
public class JwtProvider implements TokenProvider {

    private final SecretKey secretKey;

    public JwtProvider(@Value("${auth.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(
            secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    public String createToken(Member member) {
        return Jwts.builder()
            .claim("memberId", member.getId())
            .claim("role", member.getRole())
            .signWith(secretKey)
            .compact();
    }

    public Long getMemberId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // 검증
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberId", Long.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
            .verifyWith(secretKey) // 검증
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("role", String.class);
    }
}
