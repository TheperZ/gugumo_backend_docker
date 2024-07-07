package sideproject.gugumo.domain.entity.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sideproject.gugumo.domain.entity.member.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmNotificationToken {

    @Id
    @GeneratedValue
    @Column(name = "fcm_token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String token;


    //2달 주기로 토큰 삭제 용
    @Builder.Default
    private LocalDateTime lastUsedDate = LocalDateTime.now();


    public void updateDate() {
        this.lastUsedDate = LocalDateTime.now();
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
