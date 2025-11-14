package kjstyle.jwtloginsample.exceptions;

/**
 * 로그인 시 잘못된 자격증명(userId 또는 password)으로 인해 발생하는 예외
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("사용자 ID 또는 비밀번호가 일치하지 않습니다.");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
