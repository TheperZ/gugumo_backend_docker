/**
 * 삭제 예정
 */

//package sideproject.gugumo.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//import sideproject.gugumo.response.ApiResponse;
//
//import java.io.IOException;
//
//@Slf4j
//public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//
//    private ObjectMapper mapper = new ObjectMapper();
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//
//        String errorMessage;
//
//        if(exception instanceof InternalAuthenticationServiceException) {
//            errorMessage = "아이디 혹은 비밀번호가 틀렸습니다.";
//        }
//        else {
//            errorMessage = "아이디 혹은 비밀번호가 틀렸습니다.";
//        }
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("utf-8");
//
//        ApiResponse<String> fail = ApiResponse.createFail(errorMessage);
//        String result = mapper.writeValueAsString(fail);
//
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        response.getWriter().write(result);
//
//        log.info("로그인 실패");
//    }
//}
