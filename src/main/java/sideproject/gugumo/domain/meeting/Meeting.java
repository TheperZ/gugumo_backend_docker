package sideproject.gugumo.domain.meeting;

import jakarta.persistence.*;
import lombok.*;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.post.Post;
import sideproject.gugumo.request.UpdatePostReq;


import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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
    private LocalDateTime meetingDeadline;

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
        this.meetingDateTime=mergeDatetime(updatePostReq.getMeetingDate(), updatePostReq.getMeetingTime());
        this.meetingDeadline = updatePostReq.getMeetingDeadline();
        this.status = MeetingStatus.valueOf(updatePostReq.getStatus());
        this.meetingMemberNum = updatePostReq.getMeetingMemberNum();
        this.openKakao = updatePostReq.getOpenKakao();


    }


    /**
     *
     * @param meetingDate
     * @param meetingTime: "xx시"로 간주->추후 수정될 수 있음
     * @return
     */
    private LocalDateTime mergeDatetime(LocalDate meetingDate, String meetingTime) {
        return meetingDate.atStartOfDay().plusHours(Integer.parseInt(meetingTime.substring(0, 2)));

    }
}
