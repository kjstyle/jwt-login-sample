package kjstyle.jwtloginsample.auth;

import kjstyle.jwtloginsample.auth.dto.LoginUserInfo;
import kjstyle.jwtloginsample.exceptions.InvalidCredentialsException;
import kjstyle.jwtloginsample.jwt.JwtUtil;
import kjstyle.jwtloginsample.user.User;
import kjstyle.jwtloginsample.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스
 * 서비스는 내부용 DTO만 반환하고, Controller가 Response DTO로 변환함
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 로그인을 처리하고 JWT 토큰을 발급합니다.
     * @param userId 사용자 ID
     * @param password 비밀번호 (평문)
     * @return 로그인 정보 (accessToken, refreshToken, 사용자정보)
     * @throws InvalidCredentialsException 사용자 없음 또는 비밀번호 불일치
     */
    public LoginUserInfo login(String userId, String password) {
        // 1. 사용자 조회
        User user = userService.getUserByUserId(userId);
        if (user == null) {
            throw new InvalidCredentialsException();
        }

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        // 3. LoginUser 객체 생성
        LoginUser loginUser = new LoginUser(user.getUserNo(), user.getUserId());

        // 4. 토큰 발급
        String accessToken = jwtUtil.createAccessToken(loginUser);
        String refreshToken = jwtUtil.createRefreshToken(loginUser);

        log.info("User logged in - userId: {}", userId);
        return new LoginUserInfo(accessToken, refreshToken, user.getUserNo(), user.getUserId());
    }
}
