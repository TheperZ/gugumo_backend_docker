package sideproject.gugumo.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.Meeting;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.meeting.MeetingType;
import sideproject.gugumo.domain.post.Post;
import sideproject.gugumo.repository.MeetingRepository;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.repository.PostRepository;
import sideproject.gugumo.request.PostReq;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(PostReq postReq) {

        //토큰에서 꺼내야 할 거 같음
        Member author = Optional.of(memberRepository.findById(postReq.getAuthorId()).get())
                .orElseThrow(NoSuchElementException::new);

        Post post = Post.builder()
                .title(postReq.getTitle())
                .content(postReq.getContent())
                .member(author)
                .build();

        postRepository.save(post);

        //meeting 저장 예정
        Meeting meeting = Meeting.builder()
                .meetingType(MeetingType.valueOf(postReq.getMeetingType()))
                .gameType(GameType.valueOf(postReq.getGameType()))
                .location(postReq.getLocation())
                .meetingDateTime(mergeDatetime(postReq.getMeetingDate(), postReq.getMeetingTime()))
                .meetingMemberNum(postReq.getMeetingMemberNum())
                .openKakao(postReq.getOpenKakao())
                .member(author)
                .post(post)
                .build();

        meetingRepository.save(meeting);
    }

    /**
     *
     * @param meetingDate
     * @param meetingTime: xx시로 간주->추후 수정될 수 있음
     * @return
     */
    private LocalDateTime mergeDatetime(LocalDate meetingDate, String meetingTime) {
        return meetingDate.atStartOfDay().plusHours(Integer.parseInt(meetingTime.substring(0, 2)));

    }

}
