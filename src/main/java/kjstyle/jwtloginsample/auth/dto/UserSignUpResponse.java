package kjstyle.jwtloginsample.auth.dto;

import kjstyle.jwtloginsample.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원가입 응답 DTO
 * 등록된 사용자의 기본 정보를 클라이언트에게 전달
 */
@Getter
@AllArgsConstructor
public class UserSignUpResponse {
    private Long userNo;
    private String userId;

    /**
     * User 엔티티를 Response DTO로 변환
     * @param user 등록된 사용자 엔티티
     * @return Response DTO
     */
    public static UserSignUpResponse from(User user) {
        return new UserSignUpResponse(user.getUserNo(), user.getUserId());
    }
}
