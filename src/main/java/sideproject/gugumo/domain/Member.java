package sideproject.gugumo.domain;

import jakarta.persistence.*;
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
    private String password;
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public Member(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }

    public Member(String username, String nickname, String password, MemberRole role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    //User를 생성할 때 일부 값을 default로 설정하기 위해서 builder 구현
    public static UserBuilder createUserBuilder() {
        return new UserBuilder();
    }


    public static MemberBuilder createMemberBuilder() {
        return new MemberBuilder();
    }

    public static class MemberBuilder {
        private String username;
        private String password;
        private String nickname;
        private MemberRole role;

        public MemberBuilder username(String username) {
            this.username = username;
            return this;
        }

        public MemberBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public MemberBuilder role(String role) {
            this.role = MemberRole.valueOf(role);
            return this;
        }

        public MemberBuilder role(MemberRole role) {
            this.role = role;
            return this;
        }

        public Member build() {
            return new Member(this.username, this.password, this.nickname, this.role);
        }
    }



    public static class UserBuilder {
        private String username;
        private String password;
        private String nickname;

        public UserBuilder username(String username) {
            this.username = username;
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
            return new Member(this.username, this.password, this.nickname, MemberRole.ROLE_USER);
        }
    }
}
