package kjstyle.jwtloginsample.interceptor;

import jakarta.servlet.http.Cookie;
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
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;
    private final JwtInterceptorHelper jwtInterceptorHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String accessToken = jwtInterceptorHelper.extractAccessTokenFromRequest(request);
            LoginUser loginUser = jwtUtil.getLoginUserFromAccessToken(accessToken);
            request.setAttribute("loginUser", loginUser);
        } catch (ExpiredTokenException ete) {
            // TODO : 만료된 토큰으로 리프레시 토큰으로 토큰 재발급하는 로직을 짜야함
        } catch (InvalidTokenException ite) {
            throw new UnauthenticatedException();
        }
        return true;
    }
}
