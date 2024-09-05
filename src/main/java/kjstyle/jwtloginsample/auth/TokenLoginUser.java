package kjstyle.jwtloginsample.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 만들어두긴했는데.. argument resolver에서 클래스타입으로 구현해서.. 좀더 해보다가 없앨 예정
 */
@Deprecated
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenLoginUser {
}
