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
import sideproject.gugumo.request.CreatePostReq;
import sideproject.gugumo.request.UpdatePostReq;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * 모든 enum 타입은 예외 처리 필요
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(CreatePostReq createPostReq) {

        //토큰에서 꺼내야 할 거 같음
        Member author = Optional.of(memberRepository.findById(createPostReq.getAuthorId()).get())
                .orElseThrow(NoSuchElementException::new);

        //post 저장
        Post post = Post.builder()
                .title(createPostReq.getTitle())
                .content(createPostReq.getContent())
                .member(author)
                .build();

        postRepository.save(post);

        //meeting 저장
        Meeting meeting = Meeting.builder()
                .meetingType(MeetingType.valueOf(createPostReq.getMeetingType()))
                .gameType(GameType.valueOf(createPostReq.getGameType()))
                .location(createPostReq.getLocation())
                .meetingDateTime(mergeDatetime(createPostReq.getMeetingDate(), createPostReq.getMeetingTime()))
                .meetingMemberNum(createPostReq.getMeetingMemberNum())
                .openKakao(createPostReq.getOpenKakao())
                .member(author)
                .post(post)
                .build();

        meetingRepository.save(meeting);
    }

    /**
     *
     * @param meetingDate
     * @param meetingTime: "xx시"로 간주->추후 협의 후 수정될 수 있음
     * @return
     */
    private LocalDateTime mergeDatetime(LocalDate meetingDate, String meetingTime) {
        return meetingDate.atStartOfDay().plusHours(Integer.parseInt(meetingTime.substring(0, 2)));

    }

    @Transactional
    public void deletePost(Long postId) {
        Post targetPost = Optional.of(postRepository.findById(postId).get())
                .orElseThrow(NoSuchElementException::new);

        //targetPost.isDelete=true
        targetPost.tempDelete();

    }

    @Transactional
    public void update(Long postId, UpdatePostReq updatePostReq) {
        Post targetPost = Optional.of(postRepository.findById(postId).get())
                .orElseThrow(NoSuchElementException::new);

        targetPost.update(updatePostReq);

        Meeting targetMeeting = Optional.of(meetingRepository.findByPost(targetPost).get())
                .orElseThrow(NoSuchElementException::new);

        targetMeeting.update(updatePostReq);

    }
}
