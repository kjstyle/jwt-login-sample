package kjstyle.jwtloginsample.exceptions;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("비정상적인 토큰이래요~");
    }
}
