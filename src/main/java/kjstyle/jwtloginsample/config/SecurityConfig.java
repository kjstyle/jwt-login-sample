package kjstyle.jwtloginsample.config;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Security 설정
 * JWT 기반 인증을 사용하므로 Spring Security의 기본 보안을 비활성화하고
 * 커스텀 인터셉터에서 모든 인증을 처리하도록 함
 */
@Configuration
public class SecurityConfig {
    // Spring Security의 자동 설정을 비활성화하려면
    // application.yml에 spring.autoconfigure.exclude 설정 필요
}
