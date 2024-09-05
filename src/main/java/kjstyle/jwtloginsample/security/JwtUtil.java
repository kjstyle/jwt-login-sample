package kjstyle.jwtloginsample.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import kjstyle.jwtloginsample.auth.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.util.Date;


@Slf4j
@Component
public class JwtUtil {

    // 터미널에 `openssl rand -hex 32` 명령어 입력시 랜덤한 값을 얻을수 있다.
    private static final String secretKey = "c294d9d9ac58c5e3c816ccf1c185c745092ff30be8f4d72ba1d7a5d99d2e3aa5";

    private SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    private static final Long EXPIRATION_TIME_MS = 1000 * 60 * 60 * 24L; // 밀리세컨이라 1000 * 60초 * 60분 * 24시 => 하루
    private static final String USER_NO_KEY_NAME = "userNo";
    private final String USER_ID_KEY_NAME = "userId";

    public String createAccessToken(LoginUser loginUser) {
        String token = Jwts.builder()
                .claim(USER_NO_KEY_NAME, loginUser.getUserNo())
                .claim(USER_ID_KEY_NAME, loginUser.getUserId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(key)
                .compact();
        log.debug("created token : {} ", token);
        return token;
    }

    public LoginUser getLoginUserFromAccessToken(String accessToken) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();

        return LoginUser.builder()
                .userNo(claims.get(USER_NO_KEY_NAME, Long.class))
                .userId(claims.get(USER_ID_KEY_NAME, String.class))
                .build();
    }
}