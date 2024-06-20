package sideproject.gugumo.domain.entity.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import sideproject.gugumo.domain.dto.memberDto.UpdateMemberDto;

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
    @JsonProperty
    MemberRole role;

    // 서비스 이용 약관 동의 여부
    Boolean isAgreeTermsUse;
    // 개인정보 수집 및 이용 동의
    Boolean isAgreeCollectingUsingPersonalInformation;
    // 마케팅 수신 동의
    Boolean isAgreeMarketing;

    @OneToMany(mappedBy = "member")
    private List<FavoriteSport> favoriteSports = new ArrayList<>();

    @Builder
    public Member(Long id, String username, String password, String nickname, String profileImagePath, MemberStatus status, MemberRole role, Boolean isAgreeTermsUse, Boolean isAgreeCollectingUsingPersonalInformation, Boolean isAgreeMarketing, List<FavoriteSport> favoriteSports) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
        this.status = status;
        this.role = role;
        this.isAgreeTermsUse = isAgreeTermsUse;
        this.isAgreeCollectingUsingPersonalInformation = isAgreeCollectingUsingPersonalInformation;
        this.isAgreeMarketing = isAgreeMarketing;
        this.favoriteSports = favoriteSports;
    }

    @Builder(builderClassName = "UserLogin", builderMethodName = "userLogin")
    public Member(Long id, String username, MemberRole role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    @Builder(builderClassName = "UserJoin", builderMethodName = "userJoin")
    public Member(Long id, String username, String password, String nickname, Boolean isAgreeTermsUse, Boolean isAgreeCollectingUsingPersonalInformation, Boolean isAgreeMarketing, List<FavoriteSport> favoriteSports) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.isAgreeTermsUse = isAgreeTermsUse;
        this.isAgreeCollectingUsingPersonalInformation = isAgreeCollectingUsingPersonalInformation;
        this.isAgreeMarketing = isAgreeMarketing;
        this.favoriteSports = favoriteSports;
        this.role = MemberRole.ROLE_USER;
        this.status = MemberStatus.active;
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