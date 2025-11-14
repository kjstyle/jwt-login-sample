package kjstyle.jwtloginsample.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 서비스 계층에서 사용하는 로그인 정보 DTO
 * 토큰과 사용자 정보를 포함하며, Controller가 이를 Response DTO로 변환
 */
@Getter
@AllArgsConstructor
public class LoginUserInfo {
    private String accessToken;
    private String refreshToken;
    private Long userNo;
    private String userId;
}
