package kjstyle.jwtloginsample.config;

import kjstyle.jwtloginsample.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class JwtPropertiesTest extends BaseTest {

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    void 애플리케이션설정값이_프로퍼티에_바인딩되어야한다() {
        assertThat(jwtProperties.getSecret()).isEqualTo("dgdE7unlvo0lzoEzDtCbjcBH90062kVW");
        assertThat(jwtProperties.getExpiration()).isEqualTo(86_400_000L);
        assertThat(jwtProperties.getRefreshExpiration()).isEqualTo(604_800_000L);
    }
}
