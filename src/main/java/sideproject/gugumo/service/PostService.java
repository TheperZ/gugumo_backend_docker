package sideproject.gugumo.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.cond.PostSearchCondition;
import sideproject.gugumo.cond.SortType;
import sideproject.gugumo.domain.meeting.*;
import sideproject.gugumo.dto.CustomUserDetails;
import sideproject.gugumo.dto.detailpostdto.DetailPostDto;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.post.Post;
import sideproject.gugumo.dto.simplepostdto.SimplePostDto;
import sideproject.gugumo.dto.detailpostdto.LongDetailPostDto;
import sideproject.gugumo.dto.detailpostdto.ShortDetailPostDto;
import sideproject.gugumo.dto.simplepostdto.SimpleTransLongDto;
import sideproject.gugumo.dto.simplepostdto.SimpleTransPostDto;
import sideproject.gugumo.dto.simplepostdto.SimpleTransShortDto;
import sideproject.gugumo.exception.NoAuthorizationException;
import sideproject.gugumo.exception.exception.PostNotFoundException;
import sideproject.gugumo.page.PageCustom;
import sideproject.gugumo.repository.BookmarkRepository;
import sideproject.gugumo.repository.MeetingRepository;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.repository.PostRepository;
import sideproject.gugumo.request.CreatePostReq;
import sideproject.gugumo.request.UpdatePostReq;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 모든 enum 타입은 예외 처리 필요
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
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
    public void save(CustomUserDetails principal, CreatePostReq createPostReq) {

        /**
         *  orElse~를 사용하는 경우 null이 아닐 시 Optional의 인자가 반환된다.
         */

        //if principal==null->로그인을 하지 않아 principal 이 없음->권한이 없습니다 exception
        if (principal == null) {
            throw new NoAuthorizationException("저장 실패: 게시글 저장 권한이 없습니다.");
        }

        //토큰에서
        Member author=memberRepository.findByUsername(principal.getUsername())
                .orElseThrow(()->
                        new NoAuthorizationException("저장 실패: 게시글 저장 권한이 없습니다.")
                );


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

    public <T extends SimpleTransPostDto> PageCustom<T> findSimplePost(CustomUserDetails principal, Pageable pageable, String q,
                                              String gameType, String location, String meetingStatus, String sortType) {
        PostSearchCondition condition = PostSearchCondition.builder()
                .q(q)
                .gameType(gameType == null ? null : GameType.valueOf(gameType))
                .location(location == null ? null : Location.valueOf(location))
                .meetingStatus(meetingStatus.equals("ALL") ? null : MeetingStatus.valueOf(meetingStatus))
                .sortType(SortType.valueOf(sortType))
                .build();


        Page<SimplePostDto> page = postRepository.search(condition, pageable, principal);


        //이걸 한번 더 가공해서(DetailPostDto처럼, 단기모임에서는 meetingDatetime을, 장기모임에서는 meetingTime, meetingDays)
        List<T> result = page.stream()
                .map(p -> convertToTransDto(p))
                .map(r -> (T) r)
                .collect(Collectors.toList());


        return new PageCustom<>(result, page.getPageable(), page.getTotalElements());


    }

    private <T extends SimpleTransPostDto> T convertToTransDto(SimplePostDto s) {

        if (s.getMeetingType() == MeetingType.SHORT) {
            SimpleTransShortDto result = SimpleTransShortDto.builder()
                    .postId(s.getPostId())
                    .meetingStatus(s.getStatus())
                    .gameType(s.getGameType())
                    .location(s.getLocation())
                    .title(s.getTitle())
                    .meetingMemberNum(s.getMeetingMemberNum())
                    .meetingDeadline(s.getMeetingDeadline())
                    .isBookmarked(s.isBookmarked())
                    .meetingDateTime(s.getMeetingDateTime())
                    .build();
            return (T)result;


        } else if (s.getMeetingType() == MeetingType.LONG) {
            SimpleTransLongDto result = SimpleTransLongDto.builder()
                    .postId(s.getPostId())
                    .meetingStatus(s.getStatus())
                    .gameType(s.getGameType())
                    .location(s.getLocation())
                    .title(s.getTitle())
                    .meetingMemberNum(s.getMeetingMemberNum())
                    .meetingDeadline(s.getMeetingDeadline())
                    .isBookmarked(s.isBookmarked())
                    .meetingTime(s.getMeetingDateTime().toLocalTime())
                    .meetingDays(s.getMeetingDays())
                    .build();

            return (T) result;


        } else {
            //TODO: 해당 타입의 게시글이 없습니다Exception
            return null;
        }
    }


    //장기, 단기에 따라 dto를 나눠서 전송
    @Transactional          //viewCount++가 동작하므로 readonly=false
    public <T extends DetailPostDto> T findDetailPostByPostId(CustomUserDetails principal, Long postId) {


        Post targetPost = postRepository.findByIdAndIsDeleteFalse(postId)
                .orElseThrow(()->new PostNotFoundException("해당 게시글이 존재하지 않습니다."));


        Meeting targetMeeting = targetPost.getMeeting();

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
                    .isYours(
                            principal != null && principal.getUsername().equals(targetPost.getMember().getUsername())
                    )
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
                    .isYours(
                            principal != null && principal.getUsername().equals(targetPost.getMember().getUsername()))
                    .bookmarkCount(bookmarkRepository.countByPost(targetPost))
                    .build();

            return (T) detailPostDto;
        } else {
            //TODO: 해당 타입의 게시글이 없습니다Exception
            return null;

        }


    }

    @Transactional
    public void update(CustomUserDetails principal, Long postId, UpdatePostReq updatePostReq) {
        //토큰에서
        if (principal == null) {
            throw new NoAuthorizationException("수정 실패: 게시글 수정 권한이 없습니다.");
        }

        Member member=memberRepository.findByUsername(principal.getUsername())
                .orElseThrow(()->
                        new NoAuthorizationException("수정 실패: 게시글 수정 권한이 없습니다.")
                );

        Post targetPost =postRepository.findByIdAndIsDeleteFalse(postId)
                .orElseThrow(()->new PostNotFoundException("수정 실패: 해당 게시글이 존재하지 않습니다."));

        //post의 member 동일인 여부 확인
        if (!targetPost.getMember().equals(member)) {
            throw new NoAuthorizationException("수정 실패: 게시글 수정 권한이 없습니다.");
        }

        targetPost.update(updatePostReq);

        Meeting targetMeeting = targetPost.getMeeting();

        targetMeeting.update(updatePostReq);

    }

    @Transactional
    public void deletePost(CustomUserDetails principal, Long postId) {

        //토큰에서
        if (principal == null) {
            throw new NoAuthorizationException("삭제 실패: 게시글 삭제 권한이 없습니다.");
        }

        Member member=memberRepository.findByUsername(principal.getUsername())
                .orElseThrow(()->
                        new NoAuthorizationException("삭제 실패: 게시글 삭제 권한이 없습니다.")
                );


        Post targetPost = postRepository.findByIdAndIsDeleteFalse(postId)
                .orElseThrow(()->new PostNotFoundException("삭제 실패: 해당 게시글이 존재하지 않습니다."));

        //post의 member 동일인 여부 확인
        if (!targetPost.getMember().equals(member)) {
            throw new NoAuthorizationException("삭제 실패: 게시글 삭제 권한이 없습니다.");
        }

        //targetPost.isDelete=true
        targetPost.tempDelete();

    }

    public <T extends SimpleTransPostDto> PageCustom<T> findMyPost(CustomUserDetails principal, Pageable pageable) {

        //토큰에서
        if (principal == null) {
            throw new NoAuthorizationException("접근 권한이 없습니다.");
        }

        Member member=memberRepository.findByUsername(principal.getUsername())
                .orElseThrow(()->
                        new NoAuthorizationException("접근 권한이 없습니다.")
                );

        Page<Post> page = postRepository.findByMemberAndIsDeleteFalse(pageable, member);

        List<T> result = page.stream()
                .map(p -> convertToTransDto(p, member))
                .map(r->(T)r)
                .collect(Collectors.toList());

        return new PageCustom<>(result, page.getPageable(), page.getTotalElements());

    }

    private <T extends SimpleTransPostDto> T convertToTransDto(Post post, Member member) {

        Meeting meeting = post.getMeeting();


        if (post.getMeeting().getMeetingType() == MeetingType.SHORT) {
            SimpleTransShortDto result = SimpleTransShortDto.builder()
                    .postId(post.getId())
                    .meetingStatus(meeting.getStatus())
                    .gameType(meeting.getGameType())
                    .location(meeting.getLocation())
                    .title(post.getTitle())
                    .meetingMemberNum(meeting.getMeetingMemberNum())
                    .meetingDeadline(meeting.getMeetingDeadline())
                    .isBookmarked(bookmarkRepository.existsByMemberAndPost(member, post))
                    .meetingDateTime(meeting.getMeetingDateTime())
                    .build();
            return (T)result;


        } else if (post.getMeeting().getMeetingType() == MeetingType.LONG) {
            SimpleTransLongDto result = SimpleTransLongDto.builder()
                    .postId(post.getId())
                    .meetingStatus(meeting.getStatus())
                    .gameType(meeting.getGameType())
                    .location(meeting.getLocation())
                    .title(post.getTitle())
                    .meetingMemberNum(meeting.getMeetingMemberNum())
                    .meetingDeadline(meeting.getMeetingDeadline())
                    .isBookmarked(bookmarkRepository.existsByMemberAndPost(member, post))
                    .meetingTime(meeting.getMeetingDateTime().toLocalTime())
                    .meetingDays(meeting.getMeetingDays())
                    .build();

            return (T) result;


        } else {
            //TODO: 해당 타입의 게시글이 없습니다Exception
            return null;
        }


    }


}
