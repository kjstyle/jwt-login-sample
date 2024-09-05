package kjstyle.jwtloginsample.auth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class LoginUser {
    private final long userNo;

    private final String userId;
}
