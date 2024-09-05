package kjstyle.jwtloginsample.security;

import kjstyle.jwtloginsample.auth.LoginUser;
import kjstyle.jwtloginsample.common.BaseTest;
import kjstyle.jwtloginsample.exceptions.InvalidTokenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class JwtUtilTest extends BaseTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("토큰생성해보기")
    void 토큰생성해보기() {
        String newToken = jwtUtil.createAccessToken(new LoginUser(7L, "kjstyile"));
        Assertions.assertNotEquals("", newToken);
    }

    @Test
    @DisplayName("토큰에서_회원번호_꺼내보기")
    void 토큰에서_회원번호_꺼내보기() {
        String accessToken = jwtUtil.createAccessToken(new LoginUser(7L, "kjstyile"));
        LoginUser loginUser = jwtUtil.getLoginUserFromAccessToken(accessToken);
        Assertions.assertEquals(7L,loginUser.getUserNo());
    }

    @Test
    @DisplayName("비정상토큰일 경우 InvalidTokenException이 나는지")
    void 비정상토큰이면특정예외발생() {
        Assertions.assertThrows(InvalidTokenException.class, () -> {
            String invalidToekn = "나는 토큰이 아니무니다";
            jwtUtil.getLoginUserFromAccessToken(invalidToekn);
        });
    }
}