package kjstyle.jwtloginsample.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import kjstyle.jwtloginsample.auth.LoginUser;
import kjstyle.jwtloginsample.common.BaseMockMvcTest;
import kjstyle.jwtloginsample.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SampleControllerTest extends BaseMockMvcTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;



    @Test
    void 토큰생성해서_헤더에_토큰넣고_에코컨트롤러_호출해서_정상인지_확인하기() throws Exception {
        LoginUser testLoginUser = new LoginUser(7,  "kjstyle");

        String testAccessToken = jwtUtil.createAccessToken(testLoginUser);

        final ResultActions actions = mockMvc.perform(get("/echo-login-user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + testAccessToken)
        );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.userNo").value(testLoginUser.getUserNo()))
                .andExpect(jsonPath("$.userId").value(testLoginUser.getUserId()));
    }

    @Test
    void 비정상토큰을_헤더에넣고_보내면_401에러_나야함() throws Exception {
        String invalidToken = "나는 토큰이 아니무니다";

        final ResultActions actions = mockMvc.perform(get("/echo-login-user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + invalidToken)
        );

        actions.andExpect(status().isUnauthorized());
    }
}