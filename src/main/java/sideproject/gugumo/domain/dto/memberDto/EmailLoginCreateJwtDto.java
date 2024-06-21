package sideproject.gugumo.domain.dto.memberDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailLoginCreateJwtDto {

    private Long id;
    private String username;
    private String role;
    private Long requestTimeMs;

    @Builder
    public EmailLoginCreateJwtDto(Long id, String username, String role, Long requestTimeMs) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.requestTimeMs = requestTimeMs;
    }
}
