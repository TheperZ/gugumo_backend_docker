package sideproject.gugumo.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String profileImagePath;
    @Enumerated(EnumType.STRING)
    MemberStatus status;

    @Builder
    public Member(String email, String password, String nickname, MemberStatus status) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(email, member.email) && Objects.equals(password, member.password) && Objects.equals(nickname, member.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, nickname);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profileImagePath='" + profileImagePath + '\'' +
                ", status=" + status +
                '}';
    }
}