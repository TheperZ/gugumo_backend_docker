package sideproject.gugumo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sideproject.gugumo.filter.JsonUsernamePasswordAuthenticationFilter;
import sideproject.gugumo.filter.JwtFilter;
import sideproject.gugumo.jwt.JwtUtil;
import sideproject.gugumo.filter.LoginFilter;
import sideproject.gugumo.handler.LoginFailureHandler;
import sideproject.gugumo.handler.LoginSuccessJwtProviderHandler;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // filter 생성 및 경로 수정(UsernamePasswordAuthenticationFilter를
        // 수정한거기 때문에 default 인증 경로가 /login 이어서 setFilterProcessesUrl 메소드를 통해 경로 수정
//        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
//        loginFilter.setFilterProcessesUrl("/api/v1/login");

        LoginSuccessJwtProviderHandler loginSuccessJwtProviderHandler = new LoginSuccessJwtProviderHandler(jwtUtil);
        LoginFailureHandler loginFailureHandler = new LoginFailureHandler();

        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter = new JsonUsernamePasswordAuthenticationFilter(authenticationManager(authenticationConfiguration), objectMapper, jwtUtil);
        jsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessJwtProviderHandler);
        jsonUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
        ;

        // session disable
        http
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;

        // logout
        http
                .logout((logout)->logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
        ;

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/login", "/").permitAll()
                        .requestMatchers("/user").hasRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
        ;

        http
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
//                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jsonUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
}
