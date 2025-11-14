package kjstyle.jwtloginsample.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User 엔티티를 위한 JPA Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * userId로 사용자 조회
     * @param userId
     * @return 사용자 정보
     */
    Optional<User> findByUserId(String userId);
}
