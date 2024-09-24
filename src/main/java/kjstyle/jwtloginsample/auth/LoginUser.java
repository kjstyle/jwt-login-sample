package kjstyle.jwtloginsample.auth;


import lombok.*;

/**
 * 컨트롤러에 넘겨줄 로그인한회원의 정보를 담는 용도
 */
@Getter
@ToString
public class LoginUser {
    @NonNull
    private long userNo;

    @NonNull
    private String userId;

    public LoginUser(long userNo, String userId) {
        this.userNo = userNo;
        this.userId = userId;
    }
}