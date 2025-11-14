package kjstyle.jwtloginsample.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 응답 DTO
 * Controller에서 클라이언트에게 전달할 데이터 형식을 정의
 */
@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long userNo;
    private String userId;

    /**
     * 서비스 계층의 DTO를 Response DTO로 변환
     * @param loginUserInfo 서비스에서 반환한 로그인 정보
     * @return Response DTO
     */
    public static LoginResponse from(LoginUserInfo loginUserInfo) {
        return new LoginResponse(
            loginUserInfo.getAccessToken(),
            loginUserInfo.getRefreshToken(),
            loginUserInfo.getUserNo(),
            loginUserInfo.getUserId()
        );
    }
}
