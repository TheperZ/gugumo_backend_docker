package sideproject.gugumo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import sideproject.gugumo.response.ApiResponse;

import java.io.IOException;

//TODO 로그인 실패시 에러코드 반환하도록 수정
@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ApiResponse<String> fail = ApiResponse.createFail("아이디 혹은 비밀번호가 틀렸습니다.");
        String result = mapper.writeValueAsString(fail);

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(result);

        log.info("로그인 실패");
    }
}
