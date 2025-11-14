package kjstyle.jwtloginsample.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security")
@Validated
public class JwtProperties {
    /**
     * signing secret injected from configuration
     */
    @NotBlank
    private String secret;

    /**
     * access token expiration in milliseconds
     */
    @Positive
    private long expiration = 1000L * 60 * 60 * 24; // default 1 day

    /**
     * refresh token expiration in milliseconds
     */
    @Positive
    private long refreshExpiration = 1000L * 60 * 60 * 24 * 7; // default 7 days
}
