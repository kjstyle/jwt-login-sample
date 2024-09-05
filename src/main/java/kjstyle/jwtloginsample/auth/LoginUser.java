package kjstyle.jwtloginsample.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class LoginUser {
    private final long userNo;

    private final String userId;

    @Builder
    public LoginUser(long userNo, String userId) {
        this.userNo = userNo;
        this.userId = userId;
    }
}
