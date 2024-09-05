package kjstyle.jwtloginsample.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 컨트롤러에 넘겨줄 로그인한회원의 정보를 담는 용도
 */
@Getter
@ToString
@Builder
public class LoginUser {
    private final long userNo;

    private final String userId;

    @Builder
    public LoginUser(final long userNo, final String userId) {
        this.userNo = userNo;
        this.userId = userId;
    }
}