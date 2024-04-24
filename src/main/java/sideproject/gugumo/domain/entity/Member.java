package sideproject.gugumo.domain.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @Enumerated(EnumType.STRING)
    MemberRole role;

    public Member(String email, String password, String nickname, String profileImagePath, MemberStatus status, MemberRole role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
        this.status = status;
        this.role = role;
    }

    public static UserBuilder createUserBuilder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private String email;
        private String password;
        private String nickname;

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Member build() {
            return new Member(this.email, this.password, this.nickname, "/default", MemberStatus.active, MemberRole.ROLE_USER);
        }
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