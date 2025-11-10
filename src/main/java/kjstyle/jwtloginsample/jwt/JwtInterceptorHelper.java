package kjstyle.jwtloginsample.jwt;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import kjstyle.jwtloginsample.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtInterceptorHelper {

    /**
     * 토큰이 헤더에 있으면 헤더에서 꺼내고, 헤더에 없으면 쿠키에서 꺼내서 리턴해줌
     * @param request
     * @return
     *   성공 : 토큰 문자열
     *   실패 : 토큰이 없으면 비정상토큰으로 예외던짐
     */
    public String extractAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // 스펙 맞는 Authorization 헤더면 Bearer 접두어 제거 후 반환
            return bearerToken.substring(7);
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("AUTH_ACCESS_TOKEN".equals(c.getName())) {
                        // 헤더가 없을 경우 쿠키에서 동일한 토큰을 꺼냄
                        return c.getValue();
                    }
                }
            }
        }
        throw new InvalidTokenException();
    }

    /**
     * 리프레시 토큰을 헤더에서 꺼내거나, 쿠키에서 꺼내서 리턴해줌
     * @param request
     * @return
     *   성공 : 리프레시 토큰 문자열
     *   실패 : 토큰이 없으면 null 리턴
     */
    public String extractRefreshTokenFromRequest(HttpServletRequest request) {
        String refreshTokenHeader = request.getHeader("X-Refresh-Token");
        if (StringUtils.hasText(refreshTokenHeader)) {
            // 커스텀 헤더에 리프레시 토큰이 오면 우선 사용
            return refreshTokenHeader;
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("AUTH_REFRESH_TOKEN".equals(c.getName())) {
                        // 쿠키에 있을 경우에도 동일하게 반환
                        return c.getValue();
                    }
                }
            }
        }
        return null; // 리프레시 토큰이 없으면 null 리턴
    }
}
