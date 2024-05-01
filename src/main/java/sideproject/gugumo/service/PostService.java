package sideproject.gugumo.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.cond.PostSearchCondition;
import sideproject.gugumo.domain.meeting.*;
import sideproject.gugumo.dto.detailpostdto.DetailPostDto;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.post.Post;
import sideproject.gugumo.dto.SimplePostDto;
import sideproject.gugumo.dto.detailpostdto.LongDetailPostDto;
import sideproject.gugumo.dto.detailpostdto.ShortDetailPostDto;
import sideproject.gugumo.repository.BookmarkRepository;
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
    private final BookmarkRepository bookmarkRepository;

    /**
     * 단기모집 기준: meetingDate, meetingTime 반영(default)
     * 장기모집일 경우 meetingDays(요일), meetingTime(1970/1/1을 쓰레기값으로)을 반영해야함
     * @param createPostReq
     */
    @Transactional
    public void save(/*CustomUserDetails principal*/CreatePostReq createPostReq) {

        /**
         *  orElse~를 사용하는 경우 null이 아닐 시 Optional의 인자가 반환된다.
         */
        //토큰에서
        /*
        memberRepository.findByUsername(principal.getUsername())
        .orElseThrow(해당 회원이 없습니다Exception::new)
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

        Meeting meeting;

        if(MeetingType.valueOf(createPostReq.getMeetingType())==MeetingType.SHORT){
            meeting = Meeting.builder()
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
            meeting = Meeting.builder()
                    .meetingType(MeetingType.valueOf(createPostReq.getMeetingType()))
                    .gameType(GameType.valueOf(createPostReq.getGameType()))
                    .location(Location.valueOf(createPostReq.getLocation()))
                    .meetingDateTime(LocalDate.of(1970,1,1).atStartOfDay().plusHours(createPostReq.getMeetingTime()))       //장기모임의 경우 date를 무시
                    .meetingDays(createPostReq.getMeetingDays())
                    .meetingDeadline(createPostReq.getMeetingDeadline())
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
     * @param meetingTime: int로 간주->추후 협의 후 수정될 수 있음
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

    public Page<SimplePostDto> findSimplePost(/*CustomUserDetails principal*/Pageable pageable, String q,
                                              String gameType, String location, String meetingStatus, String sortType) {
        PostSearchCondition condition = PostSearchCondition.builder()
                .q(q)
                .gameType(GameType.valueOf(gameType))
                .location(Location.valueOf(location))
                .meetingStatus(MeetingStatus.valueOf(meetingStatus))
                .build();



        return postRepository.search(condition, pageable, sortType/*, principal*/);

/*        //북마크 여부 처리(진짜 별로같음 필드 하나 넣자고 Dto를 왜 또 만들어)
        Page<SimplePostDto> result=pageresult.map(m->
                SimplePostDto.builder()
                        .postId(m.getPostId())
                        .status(m.getStatus())
                        .gameType(m.getGameType())
                        .location(m.getLocation())
                        .title(m.getTitle())
                        .meetingDateTime(m.getMeetingDateTime())
                        .meetingDays(m.getMeetingDays())
                        .meetingMemberNum(m.getMeetingMemberNum())
                        .meetingDeadline(m.getMeetingDeadline())
*//*                        .isBookmarked(bookmarkRepository.existsByMemberAndPost(
                                memberRepository.findByUsername(principal.getUsername()),
                                postRepository.findById(m.getPostId())
                        ))*//*
                        .build()
        );*/





    }


    //장기, 단기에 따라 dto를 나눠서 전송
    @Transactional          //viewCount++가 동작하므로 readonly=false
    public <T extends DetailPostDto> T findDetailPostByPostId(/*CustomUserDetails principal*/ Long postId) {


        Post targetPost = postRepository.findByIdAndAndIsDeleteFalse(postId)
                .orElseThrow(NoSuchElementException::new);


        Meeting targetMeeting = meetingRepository.findByPost(targetPost)
                .orElseThrow(NoSuchElementException::new);

        targetPost.addViewCount();

        if (targetMeeting.getMeetingType() == MeetingType.SHORT) {
            ShortDetailPostDto detailPostDto = ShortDetailPostDto.builder()
                    .postId(targetPost.getId())
                    .author(targetPost.getMember().getNickname())
                    .meetingType(targetMeeting.getMeetingType())
                    .gameType(targetMeeting.getGameType())
                    .meetingMemberNum(targetMeeting.getMeetingMemberNum())
                    .meetingDateTime(targetMeeting.getMeetingDateTime())        //장기일 경우 1970.1.1/time
                    .meetingDeadline(targetMeeting.getMeetingDeadline())
                    .openKakao(targetMeeting.getOpenKakao())
                    .location(targetMeeting.getLocation())
                    .title(targetPost.getTitle())
                    .content(targetPost.getContent())
                    .createdDateTime(targetPost.getCreateDate())
                    .meetingStatus(targetMeeting.getStatus())
                    .viewCount(targetPost.getViewCount())
                    //.isYours(principal.getUsername().equals(post.getMember().getUsername()))
                    .bookmarkCount(bookmarkRepository.countByPost(targetPost))
                    .build();

            return (T) detailPostDto;

        } else if (targetMeeting.getMeetingType() == MeetingType.LONG) {
            LongDetailPostDto detailPostDto = LongDetailPostDto.builder()
                    .postId(targetPost.getId())
                    .author(targetPost.getMember().getNickname())
                    .meetingType(targetMeeting.getMeetingType())
                    .gameType(targetMeeting.getGameType())
                    .meetingMemberNum(targetMeeting.getMeetingMemberNum())
                    .meetingTime(targetMeeting.getMeetingDateTime().toLocalTime())        //장기일 경우 1970.1.1/time
                    .meetingDays(targetMeeting.getMeetingDays())
                    .meetingDeadline(targetMeeting.getMeetingDeadline())
                    .openKakao(targetMeeting.getOpenKakao())
                    .location(targetMeeting.getLocation())
                    .title(targetPost.getTitle())
                    .content(targetPost.getContent())
                    .createdDateTime(targetPost.getCreateDate())
                    .meetingStatus(targetMeeting.getStatus())
                    .viewCount(targetPost.getViewCount())
                    //.isYours(principal.getUsername().equals(post.getMember().getUsername()))
                    .bookmarkCount(bookmarkRepository.countByPost(targetPost))
                    .build();

            return (T) detailPostDto;
        } else {
            //TODO: 해당 타입의 게시글이 없습니다Exception
            return null;

        }


    }

    @Transactional
    public void update(/*CustomUserDetails principal*/ Long postId, UpdatePostReq updatePostReq) {
        //토큰에서
        /*
        memberRepository.findByUsername(principal.getUsername())
        .orElseThrow(해당 회원이 없습니다Exception::new)
         */

        Post targetPost =postRepository.findByIdAndAndIsDeleteFalse(postId)
                .orElseThrow(NoSuchElementException::new);

        targetPost.update(updatePostReq);

        Meeting targetMeeting = meetingRepository.findByPost(targetPost)
                .orElseThrow(NoSuchElementException::new);

        targetMeeting.update(updatePostReq);

    }

    @Transactional
    public void deletePost(/*CustomUserDetails principal*/ Long postId) {

        //토큰에서
        /*
        memberRepository.findByUsername(principal.getUsername())
        .orElseThrow(해당 회원이 없습니다Exception::new)
         */

        Post targetPost = postRepository.findByIdAndAndIsDeleteFalse(postId)
                .orElseThrow(NoSuchElementException::new);

        //targetPost.isDelete=true
        targetPost.tempDelete();

    }


}
