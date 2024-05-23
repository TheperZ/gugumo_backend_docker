package sideproject.gugumo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.dto.CustomUserDetails;
import sideproject.gugumo.domain.entity.Comment;
import sideproject.gugumo.domain.entity.Member;
import sideproject.gugumo.domain.entity.MemberStatus;
import sideproject.gugumo.domain.entity.post.Post;
import sideproject.gugumo.exception.exception.NoAuthorizationException;
import sideproject.gugumo.exception.exception.PostNotFoundException;
import sideproject.gugumo.repository.CommentRepository;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.repository.PostRepository;
import sideproject.gugumo.request.CreateCommentReq;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public void save(CreateCommentReq req, CustomUserDetails principal) {

        Member author = memberRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new NoAuthorizationException("댓글 등록 실패: 권한이 없습니다."));

        if (author.getStatus() != MemberStatus.active) {
            throw new NoAuthorizationException("댓글 등록 실패: 권한이 없습니다.");
        }

        Post targetPost = postRepository.findByIdAndIsDeleteFalse(req.getPostId())
                .orElseThrow(() -> new PostNotFoundException("댓글 등록 실패: 존재하지 않는 게시글입니다."));

        Comment parentComment = req.getParentCommentId() != null ?
                commentRepository.findById(req.getParentCommentId()).get() : null;

        Comment comment = Comment.builder()
                .post(targetPost)
                .parentComment(parentComment)
                .member(author)
                .content(req.getContent())
                .build();

        commentRepository.save(comment);
        targetPost.addCommentCnt();

    }
}
