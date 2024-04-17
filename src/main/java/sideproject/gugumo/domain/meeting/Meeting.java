package sideproject.gugumo.domain.meeting;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.post.Post;


import java.time.LocalDateTime;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Meeting {

    @Id
    @GeneratedValue
    @Column(name = "meeting_id")
    private Long id;

    private MeetingType meetingType;
    private GameType gameType;
    private String location;
    private LocalDateTime meetingDateTime;
    private LocalDateTime meetingDeadline;

    @Builder.Default
    private MeetingStatus status = MeetingStatus.RECRUIT;

    private int meetingMemberNum;
    private String openKakao;


    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "post_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Post post;
}
