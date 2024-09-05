package kjstyle.jwtloginsample.common;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc // Junit5에서는 이 어노테이션이 있어야 MockMvc주입에 문제가 안생김 (출처 : https://gofnrk.tistory.com/74)
public abstract class BaseMockMvcTest extends BaseTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach
    public void setup() {
        // 한글 깨짐 방지 처리
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                // .alwaysExpect(MockMvcResultMatchers.status().isOk()) // 모든 테스트에서 200OK를 기대하도록 설정..인데 200외 응답도 테스트하므로 주석처리해둠
                .alwaysDo(print()) // 모든 테스트에서 출력을 하도록 설정
                .build();
    }
}
