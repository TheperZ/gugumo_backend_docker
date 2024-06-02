package sideproject.gugumo.domain.entity.member;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.StringUtils;
import sideproject.gugumo.domain.dto.memberDto.UpdateMemberDto;
import sideproject.gugumo.domain.entity.meeting.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String profileImagePath;
    @Enumerated(EnumType.STRING)
    MemberStatus status;
    @Enumerated(EnumType.STRING)
    MemberRole role;

    // 서비스 이용 약관 동의 여부
    Boolean isAgreeTermsUse;
    // 개인정보 수집 및 이용 동의
    Boolean isAgreeCollectingUsingPersonalInformation;
    // 마케팅 수신 동의
    Boolean isAgreeMarketing;

    @OneToMany(mappedBy = "member")
    private List<FavoriteSport> favoriteSports = new ArrayList<>();

    public Member(String username, String password, String nickname, String profileImagePath, MemberStatus status, MemberRole role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
        this.status = status;
        this.role = role;
    }

    public void updateMemberNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateMemberPassword(String password) {
        this.password = password;
    }

    public void updateMember(UpdateMemberDto updateMemberDto) {
        this.nickname = updateMemberDto.getNickname();
        this.profileImagePath = updateMemberDto.getProfileImagePath();

        if(updateMemberDto.getPassword() != null && StringUtils.hasText(this.password = updateMemberDto.getPassword())) {
            this.password = updateMemberDto.getPassword();
        }
    }

    public void deleteMember() {
        this.status = MemberStatus.delete;
    }

    //User를 생성할 때 일부 값을 default로 설정하기 위해서 builder 구현
    public static UserBuilder createUserBuilder() {
        return new UserBuilder();
    }

    public static AdminBuilder createAdminBuilder() {
        return new AdminBuilder();
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
            return new Member(this.username, this.password, this.nickname, "/default", MemberStatus.active, this.role);
        }
    }

    public static class AdminBuilder {
        private String username;
        private String password;
        private String nickname;

        public AdminBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AdminBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AdminBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Member build() {
            return new Member(this.username, this.password, this.nickname, "/default", MemberStatus.active, MemberRole.ROLE_ADMIN);
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
            return new Member(this.username, this.password, this.nickname, "/default", MemberStatus.active, MemberRole.ROLE_USER);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(username, member.username) && Objects.equals(password, member.password) && Objects.equals(nickname, member.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, nickname);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profileImagePath='" + profileImagePath + '\'' +
                ", status=" + status +
                '}';
    }
}