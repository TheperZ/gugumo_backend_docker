package sideproject.gugumo.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.cond.PostSearchCondition;
import sideproject.gugumo.domain.meeting.*;
import sideproject.gugumo.dto.DetailPostDto;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.post.Post;
import sideproject.gugumo.dto.SimplePostDto;
import sideproject.gugumo.repository.MeetingRepository;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.repository.PostRepository;
import sideproject.gugumo.request.CreatePostReq;
import sideproject.gugumo.request.UpdatePostReq;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
     * 상속(SINGLE_TABLE)로 구현
     * @param createPostReq
     */
    @Transactional
    public void save(CreatePostReq createPostReq) {

        /**
         *  orElse~를 사용하는 경우 null이 아닐 시 Optional의 인자가 반환된다.
         */
        //토큰에서
        Member author =memberRepository.findById(createPostReq.getAuthorId())
                .orElseThrow(NoSuchElementException::new);

        //post 저장
        Post post = Post.builder()
                .title(createPostReq.getTitle())
                .content(createPostReq.getContent())
                .member(author)
                .build();

        postRepository.save(post);

        Meeting meeting;

        if(MeetingType.valueOf(createPostReq.getMeetingType())==MeetingType.SHORT){
            meeting = ShortMeeting.builder()
                    .meetingType(MeetingType.valueOf(createPostReq.getMeetingType()))
                    .gameType(GameType.valueOf(createPostReq.getGameType()))
                    .location(Location.valueOf(createPostReq.getLocation()))
                    .meetingDateTime(mergeDatetime(createPostReq.getMeetingDate(), createPostReq.getMeetingTime()))
                    .meetingDeadline(createPostReq.getMeetingDeadline())
                    .meetingMemberNum(createPostReq.getMeetingMemberNum())
                    .openKakao(createPostReq.getOpenKakao())
                    .member(author)
                    .build();

            meeting.setPost(post);
            meetingRepository.save(meeting);
        } else if (MeetingType.valueOf(createPostReq.getMeetingType()) == MeetingType.LONG) {
            meeting = LongMeeting.builder()
                    .meetingType(MeetingType.valueOf(createPostReq.getMeetingType()))
                    .gameType(GameType.valueOf(createPostReq.getGameType()))
                    .location(Location.valueOf(createPostReq.getLocation()))
                    .meetingDateTime(LocalDate.of(1970,1,1).atStartOfDay().plusHours(createPostReq.getMeetingTime()))       //장기모임의 경우 date를 무시
                    .meetingDays(createPostReq.getMeetingDays())            //이걸 짤라서 처리?(MON;WED;FRI)
                    .meetingMemberNum(createPostReq.getMeetingMemberNum())
                    .openKakao(createPostReq.getOpenKakao())
                    .member(author)
                    .build();

            meeting.setPost(post);
            meetingRepository.save(meeting);
        }


    }

    /**
     *
     * @param meetingDate
     * @param meetingTime: "xx시"로 간주->추후 협의 후 수정될 수 있음
     * @return
     */
    private LocalDateTime mergeDatetime(LocalDate meetingDate, int meetingTime) {
        return meetingDate.atStartOfDay().plusHours(meetingTime);
    }


    /**
     * 동적 쿼리를 이용하여 게시글의 정보를 반환
     * post.title, meeting.location, meeting.gametype, page번호를 확인해야함
     * @return page
     */

    public Page<SimplePostDto> findSimplePost(Pageable pageable, String q,
                                              String gameType, String location, String meetingStatus) {
        PostSearchCondition condition = PostSearchCondition.builder()
                .q(q)
                .gameType(GameType.valueOf(gameType))
                .location(Location.valueOf(location))
                .meetingStatus(MeetingStatus.valueOf(meetingStatus))
                .build();

        return postRepository.search(condition, pageable);

    }

    public DetailPostDto findDetailPostByPostId(Long postId) {


        Post targetPost = postRepository.findById(postId)
                .orElseThrow(NoSuchElementException::new);

        Meeting targetMeeting = meetingRepository.findByPost(targetPost)
                .orElseThrow(NoSuchElementException::new);

        targetPost.addViewCount();

        DetailPostDto detailPostDto = DetailPostDto.builder()
                .meetingType(targetMeeting.getMeetingType())
                .gameType(targetMeeting.getGameType())
                .meetingMemberNum(targetMeeting.getMeetingMemberNum())
/*                .meetingDateTime(targetMeeting.getMeetingDateTime())
                .meetingDays(targetMeeting.getMeetingDays())*/
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
