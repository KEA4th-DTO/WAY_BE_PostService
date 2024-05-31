package com.dto.way.post.global.utils;

import com.dto.way.post.domain.common.Uuid;
import com.dto.way.post.repository.UuidRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;


    // HttpRequest의 Authorization header에서 Bearer prefix를 제거한 토큰을 뽑아낸다.
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    //  파라미터로 위 메서드로 뽑아낸 토큰 받아서 subject인 email을 리턴함.
    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


    //  파라미터로 위 메서드로 뽑아낸 토큰 받아서 Claims(토큰을 생성할 떄 넣은 메타 데이터)를 리턴함.
    public Claims getClaim(String jwtToken) {

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    // 파라미터로 HttpServletRequest를 받아서 memberId 클레임 값을 리턴함.
    public Long getMemberIdFromRequest(HttpServletRequest request) {
        String jwtToken = getJwtFromHeader(request);
        if (jwtToken != null) {
            Claims claims = getClaim(jwtToken);
            return claims.get("memberId", Long.class);
        }
        return null;
    }

    // 파라미터로 HttpServletRequest를 받아서 memberNickname 클레임 값을 리턴함.
    public String getMemberNicknameFromRequest(HttpServletRequest request) {
        String jwtToken = getJwtFromHeader(request);
        if (jwtToken != null) {
            Claims claims = getClaim(jwtToken);
            return claims.get("nickname", String.class);
        }
        return null;
    }

    @Component
    @RequiredArgsConstructor
    public static class UuidCreator {

        private final UuidRepository uuidRepository;

        public String createUuid() {
            String uuid = UUID.randomUUID().toString();
            Uuid savedUuid = uuidRepository.save(Uuid.builder()
                    .uuid(uuid)
                    .build());
            return savedUuid.getUuid();
        }
    }
}
