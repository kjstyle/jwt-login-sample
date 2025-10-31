package kjstyle.jwtloginsample.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kjstyle.jwtloginsample.auth.LoginUser;
import kjstyle.jwtloginsample.exceptions.ExpiredTokenException;
import kjstyle.jwtloginsample.exceptions.InvalidTokenException;
import kjstyle.jwtloginsample.exceptions.UnauthenticatedException;
import kjstyle.jwtloginsample.jwt.JwtInterceptorHelper;
import kjstyle.jwtloginsample.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;
    private final JwtInterceptorHelper jwtInterceptorHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        try {
            String accessToken = jwtInterceptorHelper.extractAccessTokenFromRequest(request);
            LoginUser loginUser = jwtUtil.getLoginUserFromAccessToken(accessToken);
            request.setAttribute("loginUser", loginUser);
        } catch (ExpiredTokenException ete) {
            // 만료된 액세스 토큰 -> 리프레시 토큰으로 재발급 시도
            handleExpiredAccessToken(request, response);
        } catch (InvalidTokenException ite) {
            throw new UnauthenticatedException();
        }
        return true;
    }

    /**
     * 만료된 액세스 토큰 처리: 리프레시 토큰으로 새로운 액세스 토큰 재발급
     * @param request
     * @param response
     */
    private void handleExpiredAccessToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 1. 리프레시 토큰 추출
            String refreshToken = jwtInterceptorHelper.extractRefreshTokenFromRequest(request);
            if (refreshToken == null) {
                throw new UnauthenticatedException();
            }

            // 2. 리프레시 토큰 검증 및 사용자 정보 추출
            LoginUser loginUser = jwtUtil.getLoginUserFromRefreshToken(refreshToken);

            // 3. 새로운 액세스 토큰 생성
            String newAccessToken = jwtUtil.createAccessToken(loginUser);

            // 4. 응답 헤더에 새 액세스 토큰 설정
            response.setHeader("X-New-Access-Token", newAccessToken);

            // 5. request에 loginUser 설정 (컨트롤러에서 사용할 수 있도록)
            request.setAttribute("loginUser", loginUser);

        } catch (ExpiredTokenException e) {
            // 리프레시 토큰도 만료됨 -> 재로그인 필요
            throw new UnauthenticatedException();
        } catch (InvalidTokenException e) {
            // 리프레시 토큰이 유효하지 않음
            throw new UnauthenticatedException();
        }
    }
}
