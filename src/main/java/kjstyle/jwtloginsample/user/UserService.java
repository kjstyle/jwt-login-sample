package kjstyle.jwtloginsample.user;

import kjstyle.jwtloginsample.exceptions.DuplicateUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스
 * Domain 계층으로 저장소 접근을 추상화하고,
 * 예외는 호출처의 맥락에 따라 처리하도록 Optional을 반환
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 새로운 사용자를 등록합니다.
     * @param userId 사용자 ID
     * @param password 비밀번호 (평문)
     * @return 등록된 사용자 정보
     * @throws DuplicateUserException 이미 존재하는 사용자 ID인 경우
     */
    public User signUp(String userId, String password) {
        // 1. 중복 체크
        if (userRepository.findByUserId(userId).isPresent()) {
            throw new DuplicateUserException();
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 3. 사용자 생성 및 저장
        User user = new User(userId, encodedPassword);
        User savedUser = userRepository.save(user);

        log.info("New user registered - userId: {}", userId);
        return savedUser;
    }

    /**
     * 사용자 번호로 사용자 정보를 조회합니다.
     * @param userNo 사용자 번호
     * @return 사용자 정보 (Optional)
     */
    public Optional<User> findByNo(Long userNo) {
        return userRepository.findById(userNo);
    }

    /**
     * 사용자 ID로 사용자 정보를 조회합니다.
     * @param userId 사용자 ID
     * @return 사용자 정보 (Optional)
     */
    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
}
