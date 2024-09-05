package kjstyle.jwtloginsample.sample;

import kjstyle.jwtloginsample.auth.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class SampleController {

    @GetMapping(value = "/login-user")
    public LoginUser arguemntResolverTest(LoginUser loginUser) {
        log.info("loginUser : " + loginUser);
        return loginUser;
    }
}