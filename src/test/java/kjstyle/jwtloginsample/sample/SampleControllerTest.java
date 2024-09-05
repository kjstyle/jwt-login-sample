package kjstyle.jwtloginsample.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import kjstyle.jwtloginsample.common.BaseMockMvcTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SampleControllerTest extends BaseMockMvcTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void arguemntResolverTest() throws Exception {
        final ResultActions actions = mockMvc.perform(get("/echo-login-user").contentType(MediaType.APPLICATION_JSON));
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.userNo").value(4))
                .andExpect(jsonPath("$.userId").value("kjstyle"));
    }
}