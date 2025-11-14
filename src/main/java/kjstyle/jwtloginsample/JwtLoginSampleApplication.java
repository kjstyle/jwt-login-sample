package kjstyle.jwtloginsample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaRepositories
public class JwtLoginSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtLoginSampleApplication.class, args);
    }

}
