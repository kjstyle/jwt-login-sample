package kjstyle.jwtloginsample.jwt;

import kjstyle.jwtloginsample.auth.LoginUser;
import kjstyle.jwtloginsample.common.BaseTest;
import kjstyle.jwtloginsample.exceptions.ExpiredTokenException;
import kjstyle.jwtloginsample.exceptions.InvalidTokenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class JwtUtilTest extends BaseTest {

    @Autowired
    private JwtUtil jwtUtil;

    private LoginUser testLoginUser = new LoginUser(7L, "kjstyle");


    @Test
    @DisplayName("토큰생성해보기")
    void 토큰생성해보기() {
        String newToken = jwtUtil.createAccessToken(testLoginUser);
        Assertions.assertNotEquals("", newToken);
    }

    @Test
    @DisplayName("토큰에서_회원번호_꺼내보기")
    void 토큰에서_회원번호_꺼내보기() {
        String accessToken = jwtUtil.createAccessToken(testLoginUser);
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

    @Test
    @DisplayName("만료된토큰의 경우 ExpiredTokenException이 나는지")
    void 만료된토큰이면특정예외발생() {
        Assertions.assertThrows(ExpiredTokenException.class, () -> {
            String accessToken = jwtUtil.createAccessToken(testLoginUser, 200L);
            Thread.sleep(1000); // 200밀리세컨후가 만료인데 1000밀리세컨 후 확인하게되니 당근 만료되었겠지?
            jwtUtil.getLoginUserFromAccessToken(accessToken);
        });
    }

    @Test
    @DisplayName("리프레시토큰 생성 테스트")
    void 리프레시토큰생성테스트() {
        String refreshToken = jwtUtil.createRefreshToken(testLoginUser);
        Assertions.assertNotEquals("", refreshToken);
    }

    @Test
    @DisplayName("리프레시토큰에서 회원정보 추출 테스트")
    void 리프레시토큰에서회원정보추출() {
        String refreshToken = jwtUtil.createRefreshToken(testLoginUser);
        LoginUser loginUser = jwtUtil.getLoginUserFromRefreshToken(refreshToken);
        Assertions.assertEquals(7L, loginUser.getUserNo());
        Assertions.assertEquals("kjstyle", loginUser.getUserId());
    }

    @Test
    @DisplayName("액세스토큰을 리프레시토큰 검증 메서드에 넣으면 InvalidTokenException 발생")
    void 액세스토큰을리프레시토큰검증메서드에넣으면예외발생() {
        Assertions.assertThrows(InvalidTokenException.class, () -> {
            String accessToken = jwtUtil.createAccessToken(testLoginUser);
            jwtUtil.getLoginUserFromRefreshToken(accessToken); // 액세스 토큰을 리프레시 토큰 검증 메서드에 넣음
        });
    }

    @Test
    @DisplayName("리프레시토큰을 액세스토큰 검증 메서드에 넣으면 InvalidTokenException 발생")
    void 리프레시토큰을액세스토큰검증메서드에넣으면예외발생() {
        Assertions.assertThrows(InvalidTokenException.class, () -> {
            String refreshToken = jwtUtil.createRefreshToken(testLoginUser);
            jwtUtil.getLoginUserFromAccessToken(refreshToken); // 리프레시 토큰을 액세스 토큰 검증 메서드에 넣음
        });
    }

    @Test
    @DisplayName("만료된 리프레시토큰의 경우 ExpiredTokenException이 나는지")
    void 만료된리프레시토큰이면특정예외발생() {
        Assertions.assertThrows(ExpiredTokenException.class, () -> {
            String refreshToken = jwtUtil.createRefreshToken(testLoginUser, 200L);
            Thread.sleep(1000); // 200밀리세컨후가 만료인데 1000밀리세컨 후 확인
            jwtUtil.getLoginUserFromRefreshToken(refreshToken);
        });
    }
}