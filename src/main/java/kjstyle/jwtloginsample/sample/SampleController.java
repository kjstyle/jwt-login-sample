package kjstyle.jwtloginsample.sample;

import kjstyle.jwtloginsample.auth.LoginUser;
import org.springframework.web.bind.annotation.*;


@RestController
public class SampleController {

    @GetMapping(value = "/login-user")
    public LoginUser arguemntResolverTest(LoginUser loginUser) {
        return loginUser;
    }
}