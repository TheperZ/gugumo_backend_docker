/**
 * 삭제 예정
 */

//package sideproject.gugumo.handler;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
//import sideproject.gugumo.jwt.JwtUtil;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Iterator;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class LoginSuccessJwtProviderHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//
//        Long id = customUserDetails.getMember().getId();
//        String username = customUserDetails.getUsername();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//
//        String role = auth.getAuthority();
//
//        String token = jwtUtil.createJwt(id, username, role, Long.parseLong(jwtUtil.getExpiredMillis()));
//
//        response.addHeader("Authorization", "Bearer " + token);
//    }
//}
