package sideproject.gugumo.security;

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
import sideproject.gugumo.jwt.JwtFilter;
import sideproject.gugumo.jwt.JwtUtil;
import sideproject.gugumo.jwt.LoginFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // filter 생성 및 경로 수정(UsernamePasswordAuthenticationFilter를
        // 수정한거기 때문에 default 인증 경로가 /login 이어서 setFilterProcessesUrl 메소드를 통해 경로 수정
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setFilterProcessesUrl("/api/v1/login");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
        ;

        http
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;

        //TODO "/api/v1/member" permitAll이 아닌 것 같음
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/login", "/api/v1/member", "/").permitAll()
                        .requestMatchers("/user").hasRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
        ;

        http
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
}
