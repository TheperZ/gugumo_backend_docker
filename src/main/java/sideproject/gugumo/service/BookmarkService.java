package sideproject.gugumo.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.Bookmark;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.meeting.Meeting;
import sideproject.gugumo.domain.post.Post;
import sideproject.gugumo.dto.BookmarkPostDto;
import sideproject.gugumo.exception.exception.BookmarkNotFoundException;
import sideproject.gugumo.exception.exception.PostNotFoundException;
import sideproject.gugumo.page.PageCustom;
import sideproject.gugumo.repository.BookmarkRepository;
import sideproject.gugumo.repository.MeetingRepository;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.repository.PostRepository;
import sideproject.gugumo.request.CreateBookmarkReq;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {


    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MeetingRepository meetingRepository;

    @Transactional
    public void save(CreateBookmarkReq req) {

        //나중에 토큰에서 가져와야 할듯
         /*
        memberRepository.findByUsername(principal.getUsername())
        .orElseThrow(해당 회원이 없습니다Exception::new)
         */
        Member member = memberRepository.findByUsername("testuser")
                .orElseThrow(NoSuchElementException::new);

        Post post = postRepository.findByIdAndIsDeleteFalse(req.getPostId())
                .orElseThrow(()->new PostNotFoundException("해당 게시글이 존재하지 않습니다."));

        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .post(post)
                .build();

        bookmarkRepository.save(bookmark);

    }

    public PageCustom<BookmarkPostDto> findBookmarkByMember(/*CustomUserDetails principal*/Pageable pageable) {

        //나중에 토큰에서 가져와야 할듯
         /*
        memberRepository.findByUsername(principal.getUsername())
        .orElseThrow(해당 회원이 없습니다Exception::new)
         */
        Member member = memberRepository.findByUsername("testuser").get();


        List<Bookmark> bookmarkList = bookmarkRepository.findByMember(member);


        List<BookmarkPostDto> result = bookmarkList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageCustom<BookmarkPostDto>(result, pageable, result.size());

    }

    private BookmarkPostDto convertToDto(Bookmark bookmark/*CustomUserDetails principal*/) {

        Post post = bookmark.getPost();
        Meeting meeting = meetingRepository.findByPost(post)
                .orElseThrow(NoSuchElementException::new);


        BookmarkPostDto result = BookmarkPostDto.builder()
                .postId(post.getId())
                .status(meeting.getStatus())
                .gameType(meeting.getGameType())
                .location(meeting.getLocation())
                .title(post.getTitle())
                .meetingDateTime(meeting.getMeetingDateTime())
                .meetingDays(meeting.getMeetingDays())
                .meetingMemberNum(meeting.getMeetingMemberNum())
                .meetingDeadline(meeting.getMeetingDeadline())
                .build();

        return result;
    }

    @Transactional
    public void delete(/*CustomUserDetails principal*/Long postId) {

        //나중에 토큰에서 가져와야 할듯
         /*
        memberRepository.findByUsername(principal.getUsername())
        .orElseThrow(해당 회원이 없습니다Exception::new)
         */

        Member testuser = memberRepository.findByUsername("testuser").get();
        Post targetPost = postRepository.findByIdAndIsDeleteFalse(postId)
                .orElseThrow(()->new PostNotFoundException("북마크 삭제 실패: 해당 게시글이 존재하지 않습니다."));

        /**
         * deleteById()와 달리 예외 처리를 커스텀할 수 있음
         */
        Bookmark bookmark = bookmarkRepository.findByMemberAndPost(testuser, targetPost)
                .orElseThrow(()->new BookmarkNotFoundException("북마크 삭제 실패: 해당 북마크가 존재하지 않습니다."));

        bookmarkRepository.delete(bookmark);
    }

}
