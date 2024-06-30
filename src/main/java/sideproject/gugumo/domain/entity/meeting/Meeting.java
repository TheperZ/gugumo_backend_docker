package sideproject.gugumo.domain.entity.meeting;

import jakarta.persistence.*;
import lombok.*;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.post.Post;
import sideproject.gugumo.request.UpdatePostReq;


import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
public class Meeting {

    @Id
    @GeneratedValue
    @Column(name = "meeting_id")
    private Long id;



    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;
    @Enumerated(EnumType.STRING)
    private GameType gameType;
    @Enumerated(EnumType.STRING)
    private Location location;

    private LocalDateTime meetingDateTime;
    private String meetingDays;
    private LocalDate meetingDeadline;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MeetingStatus status = MeetingStatus.RECRUIT;

    private int meetingMemberNum;
    private String openKakao;


    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "post_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Post post;


    /**
     * 연관관계 편의 메서드
     * @param post
     */
    public void setPost(Post post) {
        this.post = post;
        post.setMeeting(this);
    }

    public void update(UpdatePostReq updatePostReq) {
        this.meetingType = MeetingType.valueOf(updatePostReq.getMeetingType());
        this.gameType = GameType.valueOf(updatePostReq.getGameType());
        this.location = Location.valueOf(updatePostReq.getLocation());
        this.meetingDateTime = updatePostReq.getMeetingDate().atStartOfDay().plusHours(updatePostReq.getMeetingTime());
        this.meetingDays = updatePostReq.getMeetingDays();
        this.meetingDeadline = updatePostReq.getMeetingDeadline();
        this.status = MeetingStatus.valueOf(updatePostReq.getMeetingStatus());
        this.meetingMemberNum = updatePostReq.getMeetingMemberNum();
        this.openKakao = updatePostReq.getOpenKakao();


    }

    public void expireStatus() {
        this.status = MeetingStatus.END;
    }



}
