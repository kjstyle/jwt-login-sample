package kjstyle.jwtloginsample.auth;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == LoginUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 토큰을 읽어다가 디코딩해서 토큰에 들어있는 회원번호와 회원아이디를 추출해서 세팅한다고 치고
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        Object objectLoginUser = servletRequest.getAttribute("loginUser");
        if (objectLoginUser != null && objectLoginUser instanceof LoginUser) {
            return (LoginUser) objectLoginUser;
        }
        return null;
    }
}