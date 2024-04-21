package sideproject.gugumo.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.Bookmark;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.post.Post;
import sideproject.gugumo.repository.BookmarkRepository;
import sideproject.gugumo.repository.MeetingRepository;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.repository.PostRepository;
import sideproject.gugumo.request.CreateBookmarkReq;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {


    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public void save(CreateBookmarkReq req) {

        //나중에 토큰에서 가져와야 할듯
        Member member = memberRepository.findById(req.getMemberId()).orElseThrow(NoSuchElementException::new);

        Post post = postRepository.findById(req.getPostId()).orElseThrow(NoSuchElementException::new);

        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .post(post)
                .build();

        bookmarkRepository.save(bookmark);

    }

    @Transactional
    public void delete(Long bookmarkId) {

        /**
         * deleteById()와 달리 예외 처리를 커스텀할 수 있음
         */
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(NoSuchElementException::new);

        bookmarkRepository.delete(bookmark);
    }

}
