package kjstyle.jwtloginsample.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import kjstyle.jwtloginsample.auth.dto.LoginRequest;
import kjstyle.jwtloginsample.auth.dto.SignUpRequest;
import kjstyle.jwtloginsample.common.BaseMockMvcTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * AuthController 통합 테스트
 */
public class AuthControllerTest extends BaseMockMvcTest {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SIGN_UP_URL = "/open/sign-up";
    private static final String LOGIN_URL = "/open/login";
    private static final String TEST_USER_ID = "testuser";
    private static final String TEST_PASSWORD = "password123";

    @BeforeEach
    public void setUp() {
        // 각 테스트마다 DB가 초기화됨 (H2 create-drop)
    }

    @Test
    public void 회원가입_성공() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(TEST_USER_ID, TEST_PASSWORD);

        // when
        MvcResult result = mockMvc.perform(post(SIGN_UP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(TEST_USER_ID))
                .andExpect(jsonPath("$.userNo").exists())
                .andReturn();

        // then
        String response = result.getResponse().getContentAsString();
        assertThat(response).contains("testuser");
    }

    @Test
    public void 회원가입_중복_실패() throws Exception {
        // given - 첫 번째 회원가입
        SignUpRequest signUpRequest = new SignUpRequest(TEST_USER_ID, TEST_PASSWORD);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated());

        // when - 동일한 ID로 두 번째 회원가입 시도
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                // then
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void 회원가입_필수필드누락_실패() throws Exception {
        // given
        SignUpRequest invalidRequest = new SignUpRequest("", "");

        // when & then
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 로그인_성공() throws Exception {
        // given - 먼저 회원가입
        SignUpRequest signUpRequest = new SignUpRequest(TEST_USER_ID, TEST_PASSWORD);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated());

        // when
        LoginRequest loginRequest = new LoginRequest(TEST_USER_ID, TEST_PASSWORD);
        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(TEST_USER_ID))
                .andExpect(jsonPath("$.userNo").exists())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    public void 로그인_존재하지않는사용자_실패() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("nonexistent", "password");

        // when & then
        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void 로그인_비밀번호불일치_실패() throws Exception {
        // given - 먼저 회원가입
        SignUpRequest signUpRequest = new SignUpRequest(TEST_USER_ID, TEST_PASSWORD);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated());

        // when - 틀린 비밀번호로 로그인 시도
        LoginRequest loginRequest = new LoginRequest(TEST_USER_ID, "wrongpassword");
        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                // then
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void 발급받은토큰으로_인증된요청_성공() throws Exception {
        // given - 회원가입 및 로그인
        SignUpRequest signUpRequest = new SignUpRequest(TEST_USER_ID, TEST_PASSWORD);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest(TEST_USER_ID, TEST_PASSWORD);
        MvcResult loginResult = mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // 응답에서 토큰 추출
        String loginResponse = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(loginResponse).get("accessToken").asText();

        // when - 발급받은 토큰으로 인증된 엔드포인트 호출
        mockMvc.perform(post("/echo-login-user")
                .header("Authorization", "Bearer " + accessToken))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(TEST_USER_ID))
                .andExpect(jsonPath("$.userNo").exists());
    }
}
