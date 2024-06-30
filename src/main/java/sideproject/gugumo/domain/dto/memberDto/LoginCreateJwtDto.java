package sideproject.gugumo.domain.dto.memberDto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LoginCreateJwtDto {

    private Long id;
    private String username;
    private String role;
    private LocalDateTime requestTimeMs;

    @Builder
    public LoginCreateJwtDto(Long id, String username, String role, LocalDateTime requestTimeMs) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.requestTimeMs = requestTimeMs;
    }
}
