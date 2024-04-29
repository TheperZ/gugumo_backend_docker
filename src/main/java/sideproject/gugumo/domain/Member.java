package sideproject.gugumo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 연관관계를 위한 임시 작성
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    private Long memberId;
    private String username;
    private String nickname;

    public Member(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }
}
