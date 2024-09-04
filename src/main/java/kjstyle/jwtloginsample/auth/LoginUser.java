package kjstyle.jwtloginsample.auth;


public class LoginUser {
    private final long userNo;

    private final String userId;

    public LoginUser(long userNo, String userId) {
        this.userNo = userNo;
        this.userId = userId;
    }

    public long getUserNo() {
        return userNo;
    }

    public String getUserId() {
        return userId;
    }
}
