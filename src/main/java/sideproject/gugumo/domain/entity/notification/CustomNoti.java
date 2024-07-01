package sideproject.gugumo.domain.entity.notification;

import jakarta.persistence.*;
import lombok.*;
import sideproject.gugumo.domain.entity.member.Member;

import java.time.LocalDateTime;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomNoti {

    @Id
    @GeneratedValue
    @Column(name = "customnoti_id")
    private Long id;


    private String message;

    private NotificationType notificationType;

    @Builder.Default
    private boolean isRead = false;

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;      //알림 수신자

    //postNoti
    private Long postId;



    public void read() {
        this.isRead = true;
    }

}
