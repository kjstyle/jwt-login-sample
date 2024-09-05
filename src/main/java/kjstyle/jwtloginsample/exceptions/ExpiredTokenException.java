package kjstyle.jwtloginsample.exceptions;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException() {
        super("만료된 JWT 토큰입니다.");
    }
}