package sideproject.gugumo.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.entity.Bookmark;
import sideproject.gugumo.domain.entity.Member;
import sideproject.gugumo.domain.entity.meeting.Meeting;
import sideproject.gugumo.domain.entity.meeting.MeetingType;
import sideproject.gugumo.domain.entity.post.Post;
import sideproject.gugumo.domain.dto.CustomUserDetails;
import sideproject.gugumo.domain.dto.simplepostdto.SimpleTransLongDto;
import sideproject.gugumo.domain.dto.simplepostdto.SimpleTransPostDto;
import sideproject.gugumo.domain.dto.simplepostdto.SimpleTransShortDto;
import sideproject.gugumo.exception.NoAuthorizationException;
import sideproject.gugumo.exception.exception.BookmarkNotFoundException;
import sideproject.gugumo.exception.exception.DuplicateBookmarkException;
import sideproject.gugumo.exception.exception.PostNotFoundException;
import sideproject.gugumo.page.PageCustom;
import sideproject.gugumo.repository.BookmarkRepository;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.repository.PostRepository;
import sideproject.gugumo.request.CreateBookmarkReq;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {


    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public void save(CustomUserDetails principal, CreateBookmarkReq req) {

        if (principal == null) {
            throw new NoAuthorizationException("북마크 등록 실패: 비로그인 사용자입니다.");
        }

        Member member = memberRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new NoAuthorizationException("북마크 등록 실패: 권한이 없습니다."));

        Post post = postRepository.findByIdAndIsDeleteFalse(req.getPostId())
                .orElseThrow(()->new PostNotFoundException("북마크 등록 실패: 해당 게시글이 존재하지 않습니다."));

        if (bookmarkRepository.existsByMemberAndPost(member, post)) {
            throw new DuplicateBookmarkException("북마크 등록 실패: 이미 등록된 북마크입니다.");
        }

        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .post(post)
                .build();

        bookmarkRepository.save(bookmark);

    }

    public <T extends SimpleTransPostDto> PageCustom<T> findBookmarkByMember(
            CustomUserDetails principal, Pageable pageable) {

        if (principal == null) {
            throw new NoAuthorizationException("북마크 조회 실패: 비로그인 사용자입니다.");
        }


        Member member = memberRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new NoAuthorizationException("북마크 조회 실패: 권한이 없습니다."));



        Page<Bookmark> page = bookmarkRepository.findByMember(member, pageable);


        List<T> result = page.stream()
                .map(p -> convertToTransDto(p.getPost(), member))
                .map(r -> (T) r)
                .collect(Collectors.toList());

        return new PageCustom<>(result, page.getPageable(), page.getTotalElements());

    }

    //이걸 어따 놓고 쓰는게 좋을까(duplicate to postservice)
    private <T extends SimpleTransPostDto> T convertToTransDto(Post post, Member member) {

        Meeting meeting = post.getMeeting();

        SimpleTransPostDto result = new SimpleTransPostDto();

        if (post.getMeeting().getMeetingType() == MeetingType.SHORT) {
            result = SimpleTransShortDto.builder()
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


        } else if (post.getMeeting().getMeetingType() == MeetingType.LONG) {
            result = SimpleTransLongDto.builder()
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


        }
        return (T) result;

    }

    @Transactional
    public void delete(CustomUserDetails principal, Long postId) {

        if (principal == null) {
            throw new NoAuthorizationException("북마크 삭제 실패: 비로그인 사용자입니다.");
        }

        Member member = memberRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new NoAuthorizationException("북마크 삭제 실패: 권한이 없습니다."));


        Post targetPost = postRepository.findByIdAndIsDeleteFalse(postId)
                .orElseThrow(()->new PostNotFoundException("북마크 삭제 실패: 해당 게시글이 존재하지 않습니다."));

        /**
         * deleteById()와 달리 예외 처리를 커스텀할 수 있음
         */
        Bookmark bookmark = bookmarkRepository.findByMemberAndPost(member, targetPost)
                .orElseThrow(()->new BookmarkNotFoundException("북마크 삭제 실패: 해당 북마크가 존재하지 않습니다."));

        bookmarkRepository.delete(bookmark);
    }

}
