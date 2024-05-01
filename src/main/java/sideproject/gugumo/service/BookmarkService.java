package sideproject.gugumo.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.Bookmark;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.post.Post;
import sideproject.gugumo.repository.BookmarkRepository;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.repository.PostRepository;
import sideproject.gugumo.request.CreateBookmarkReq;

import java.util.NoSuchElementException;
import java.util.Optional;

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
         /*
        memberRepository.findByUsername(principal.getUsername())
        .orElseThrow(해당 회원이 없습니다Exception::new)
         */
        Member member = memberRepository.findById(req.getMemberId()).orElseThrow(NoSuchElementException::new);

        Post post = postRepository.findByIdAndAndIsDeleteFalse(req.getPostId())
                .orElseThrow(NoSuchElementException::new);

        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .post(post)
                .build();

        bookmarkRepository.save(bookmark);

    }

    public Page<Bookmark> findBookmarkByMember(/*CustomUserDetails principal*/Long memberId, Pageable pageable) {

        //나중에 토큰에서 가져와야 할듯
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);



        return bookmarkRepository.findByMember(member, pageable);
    }

    @Transactional
    public void delete(/*CustomUserDetails principal*/Long postId) {

        //나중에 토큰에서 가져와야 할듯
         /*
        memberRepository.findByUsername(principal.getUsername())
        .orElseThrow(해당 회원이 없습니다Exception::new)
         */

        Member testuser = memberRepository.findByUsername("testuser").get();
        Post targetPost = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);

        /**
         * deleteById()와 달리 예외 처리를 커스텀할 수 있음
         */
        Bookmark bookmark = bookmarkRepository.findByMemberAndPost(testuser, targetPost)
                .orElseThrow(NoSuchElementException::new);

        bookmarkRepository.delete(bookmark);
    }

}
