package kjstyle.jwtloginsample.auth;

import jakarta.validation.Valid;
import kjstyle.jwtloginsample.auth.dto.LoginRequest;
import kjstyle.jwtloginsample.auth.dto.LoginResponse;
import kjstyle.jwtloginsample.auth.dto.LoginUserInfo;
import kjstyle.jwtloginsample.auth.dto.SignUpRequest;
import kjstyle.jwtloginsample.user.User;
import kjstyle.jwtloginsample.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 관련 요청을 처리하는 컨트롤러
 * /open 경로는 인터셉터에서 제외되어 있음
 *
 * Service DTO를 Response DTO로 변환하여 응답
 */
@Slf4j
@RestController
@RequestMapping("/open")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    /**
     * 회원가입
     * @param signUpRequest 회원가입 요청
     * @return 가입된 사용자 정보
     */
    @PostMapping("/sign-up")
    public ResponseEntity<UserSignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = userService.signUp(signUpRequest.getUserId(), signUpRequest.getPassword());
        UserSignUpResponse response = new UserSignUpResponse(user.getUserNo(), user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 로그인
     * @param loginRequest 로그인 요청
     * @return 로그인 응답 (토큰 포함)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginUserInfo loginUserInfo = authService.login(loginRequest.getUserId(), loginRequest.getPassword());
        return ResponseEntity.ok(LoginResponse.from(loginUserInfo));
    }

    /**
     * 회원가입 응답 DTO (inner class)
     */
    @lombok.Getter
    @lombok.AllArgsConstructor
    static class UserSignUpResponse {
        private Long userNo;
        private String userId;
    }
}
