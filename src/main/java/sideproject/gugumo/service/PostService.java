package sideproject.gugumo.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.dto.DetailPostDto;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.Location;
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

    /**
     * 단기모집 기준: meetingDate, meetingTime 반영(default)
     * 장기모집일 경우 meetingDays(요일), meetingTime을 반영해야함
     * db 수정 후 반영 예정(아마 상속으로 구현하지 않을까)
     * @param createPostReq
     */
    @Transactional
    public void save(CreatePostReq createPostReq) {

        /**
         *  orElse~를 사용하는 경우 null이 아닐 시 Optional의 인자가 반환된다.
         */
        Member author =memberRepository.findById(createPostReq.getAuthorId())
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
                .location(Location.valueOf(createPostReq.getLocation()))
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

    public DetailPostDto findDetailPostByPostId(Long postId) {


        Post targetPost = postRepository.findById(postId)
                .orElseThrow(NoSuchElementException::new);

        Meeting targetMeeting = meetingRepository.findByPost(targetPost)
                .orElseThrow(NoSuchElementException::new);

        DetailPostDto detailPostDto = DetailPostDto.builder()
                .meetingType(targetMeeting.getMeetingType())
                .gameType(targetMeeting.getGameType())
                .meetingMemberNum(targetMeeting.getMeetingMemberNum())
                .meetingDateTime(targetMeeting.getMeetingDateTime())
                .meetingDeadline(targetMeeting.getMeetingDeadline())
                .openKakao(targetMeeting.getOpenKakao())
                .location(targetMeeting.getLocation())
                .title(targetPost.getTitle())
                .content(targetPost.getContent())
                .status(targetMeeting.getStatus())
                .viewCount(targetPost.getViewCount())
                .build();

        return detailPostDto;

    }

    @Transactional
    public void update(Long postId, UpdatePostReq updatePostReq) {
        Post targetPost =postRepository.findById(postId)
                .orElseThrow(NoSuchElementException::new);

        targetPost.update(updatePostReq);

        Meeting targetMeeting = meetingRepository.findByPost(targetPost)
                .orElseThrow(NoSuchElementException::new);

        targetMeeting.update(updatePostReq);

    }

    @Transactional
    public void deletePost(Long postId) {
        Post targetPost = postRepository.findById(postId)
                .orElseThrow(NoSuchElementException::new);

        //targetPost.isDelete=true
        targetPost.tempDelete();

    }
}
