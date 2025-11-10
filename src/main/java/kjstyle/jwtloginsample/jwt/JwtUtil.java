package kjstyle.jwtloginsample.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.Keys;
import kjstyle.jwtloginsample.auth.LoginUser;
import kjstyle.jwtloginsample.config.JwtProperties;
import kjstyle.jwtloginsample.exceptions.ExpiredTokenException;
import kjstyle.jwtloginsample.exceptions.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * jjwt 기반으로 만듬
 * 참고 : https://github.com/jwtk/jjwt
 */
@Slf4j
@Component
public class JwtUtil {

    private final SecretKey key;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;
    private static final String USER_NO_KEY_NAME = "userNo";
    private static final String USER_ID_KEY_NAME = "userId";
    private static final String TOKEN_TYPE_KEY_NAME = "tokenType";
    private static final String ACCESS_TOKEN_TYPE = "ACCESS";
    private static final String REFRESH_TOKEN_TYPE = "REFRESH";

    public JwtUtil(JwtProperties jwtProperties) {
        // 외부 설정에서 전달된 키/만료값으로 JWT 인프라 초기화
        Assert.hasText(jwtProperties.getSecret(), "JWT secret must be provided");
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMs = jwtProperties.getExpiration();
        this.refreshTokenExpirationMs = jwtProperties.getRefreshExpiration();
    }

    /**
     * 액세스 토큰생성해주는 메서드
     *   별일 없으면 이걸 사용하세요
     * @param loginUser
     * @return
     */
    public String createAccessToken(final LoginUser loginUser) {
        return this.createAccessToken(loginUser, accessTokenExpirationMs);
    }

    /**
     * 액세스 토큰생성해주는 메서드 (만료시간을 파라미터로 받는 오버로딩된 메서드)
     *  굳이 만료시간을 다르게 가져가야할 경우만 사용하도록 오버로딩해둠
     *  되도록이면  createAccessToken()를 사용해서 토큰생성바람
     * @param loginUser
     * @param expirationTimeMs
     * @return
     */
    public String createAccessToken(final LoginUser loginUser, final long expirationTimeMs) {
        // 사용자 식별자와 토큰 타입을 클레임에 담고 서명
        String token = Jwts.builder()
                .claim(USER_NO_KEY_NAME, loginUser.getUserNo())
                .claim(USER_ID_KEY_NAME, loginUser.getUserId())
                .claim(TOKEN_TYPE_KEY_NAME, ACCESS_TOKEN_TYPE)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(key)
                .compact();
        log.debug("created access token : {} ", token);
        return token;
    }

    /**
     * 리프레시 토큰생성해주는 메서드
     *   별일 없으면 이걸 사용하세요
     * @param loginUser
     * @return
     */
    public String createRefreshToken(final LoginUser loginUser) {
        return this.createRefreshToken(loginUser, refreshTokenExpirationMs);
    }

    /**
     * 리프레시 토큰생성해주는 메서드 (만료시간을 파라미터로 받는 오버로딩된 메서드)
     * @param loginUser
     * @param expirationTimeMs
     * @return
     */
    public String createRefreshToken(final LoginUser loginUser, final long expirationTimeMs) {
        // 리프레시 토큰은 동일한 클레임을 담되 타입/만료만 다르게 생성
        String token = Jwts.builder()
                .claim(USER_NO_KEY_NAME, loginUser.getUserNo())
                .claim(USER_ID_KEY_NAME, loginUser.getUserId())
                .claim(TOKEN_TYPE_KEY_NAME, REFRESH_TOKEN_TYPE)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(key)
                .compact();
        log.debug("created refresh token : {} ", token);
        return token;
    }

    /**
     * 액세스 토큰에서 로그인유저정보 꺼내오기
     * @param accessToken
     * @return
     */
    public LoginUser getLoginUserFromAccessToken(final String accessToken) {
        Claims claims = getClaims(accessToken);
        validateTokenType(claims, ACCESS_TOKEN_TYPE);
        return new LoginUser(claims.get(USER_NO_KEY_NAME, Long.class), claims.get(USER_ID_KEY_NAME, String.class));
    }

    /**
     * 리프레시 토큰에서 로그인유저정보 꺼내오기
     * @param refreshToken
     * @return
     */
    public LoginUser getLoginUserFromRefreshToken(final String refreshToken) {
        Claims claims = getClaims(refreshToken);
        validateTokenType(claims, REFRESH_TOKEN_TYPE);
        return new LoginUser(claims.get(USER_NO_KEY_NAME, Long.class), claims.get(USER_ID_KEY_NAME, String.class));
    }

    /**
     * 토큰으로부터 클레임 꺼내기 (예외처리를 위해 별도 메서드로 분리시킴)
     * @param accessToken
     * @return
     */
    private Claims getClaims(final String accessToken) {
        Claims claims ;
        try {
            // jjwt 파서를 통해 서명 검증과 페이로드 파싱을 한 번에 처리
            claims = Jwts.parser()
                    .verifyWith(key) // 단순히 key 타입만 검증하더라...
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch(ExpiredJwtException eje) { // 만료된 토큰일 경우 발생하는 Exception
            throw new ExpiredTokenException(); // 내가 만든 Exception으로 바꿔서 던짐 -> 리프레시토큰 로직으로 분기되어야함
        } catch(Exception e) { // 기타 나머지(변조되었거나, 형식이 안맞거나 등등등)는 퉁쳐서 비정상 토큰으로 간주
            throw new InvalidTokenException();
        }
        return claims;
    }

    /**
     * 토큰 타입 검증 (액세스 토큰과 리프레시 토큰을 구분하기 위함)
     * @param claims
     * @param expectedType
     */
    private void validateTokenType(final Claims claims, final String expectedType) {
        String tokenType = claims.get(TOKEN_TYPE_KEY_NAME, String.class);
        if (!expectedType.equals(tokenType)) {
            // ACCESS/REFRESH 혼용 요청은 즉시 차단
            throw new InvalidTokenException();
        }
    }
}
