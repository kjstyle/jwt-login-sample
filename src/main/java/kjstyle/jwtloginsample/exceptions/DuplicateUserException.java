package kjstyle.jwtloginsample.exceptions;

/**
 * 중복된 사용자 ID로 회원가입을 시도할 때 발생하는 예외
 */
public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
        super("이미 존재하는 사용자 ID입니다.");
    }

    public DuplicateUserException(String message) {
        super(message);
    }
}
