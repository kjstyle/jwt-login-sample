package kjstyle.jwtloginsample.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security")
public class JwtProperties {
    /**
     * signing secret injected from configuration
     */
    private String secret;

    /**
     * access token expiration in milliseconds
     */
    private long expiration = 1000L * 60 * 60 * 24; // default 1 day

    /**
     * refresh token expiration in milliseconds
     */
    private long refreshExpiration = 1000L * 60 * 60 * 24 * 7; // default 7 days
}
