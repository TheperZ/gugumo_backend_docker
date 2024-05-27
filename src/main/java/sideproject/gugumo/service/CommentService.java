package sideproject.gugumo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import sideproject.gugumo.domain.dto.CommentDto;
import sideproject.gugumo.domain.dto.CustomUserDetails;
import sideproject.gugumo.domain.entity.Comment;
import sideproject.gugumo.domain.entity.Member;
import sideproject.gugumo.domain.entity.MemberStatus;
import sideproject.gugumo.domain.entity.post.Post;
import sideproject.gugumo.exception.exception.CommentNotFoundException;
import sideproject.gugumo.exception.exception.NoAuthorizationException;
import sideproject.gugumo.exception.exception.PostNotFoundException;
import sideproject.gugumo.page.PageCustom;
import sideproject.gugumo.repository.CommentRepository;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.repository.PostRepository;
import sideproject.gugumo.request.CreateCommentReq;
import sideproject.gugumo.request.UpdateCommentReq;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public void save(CreateCommentReq req, CustomUserDetails principal) {

        Member author = checkMemberValid(principal, "댓글 등록 실패: 비로그인 사용자입니다.",
                "댓글 등록 실패: 권한이 없습니다.");

        Post targetPost = postRepository.findByIdAndIsDeleteFalse(req.getPostId())
                .orElseThrow(() -> new PostNotFoundException("댓글 등록 실패: 존재하지 않는 게시글입니다."));

        //삭제된 댓글의 대댓글도 작성할 수 있어야 함->deleteFalse를 확인하지 않음
        Comment parentComment = req.getParentCommentId() != null ?
                commentRepository.findById(req.getParentCommentId())
                        .orElseThrow(()-> new CommentNotFoundException("대댓글의 상위 댓글이 존재하지 않습니다.")) : null;

        Comment comment = Comment.builder()
                .post(targetPost)
                .parentComment(parentComment)
                .member(author)
                .content(req.getContent())
                .build();


        commentRepository.save(comment);
        targetPost.increaseCommentCnt();

    }

    public List<CommentDto> findComment(Long postId, CustomUserDetails principal, Pageable pageable) {

        return commentRepository.findComment(postId, principal, pageable);


    }

    @Transactional
    public void updateComment(Long commentId, UpdateCommentReq req, CustomUserDetails principal) {

        //member를 먼저 찾아야 equals가 동작하는 이유?
        Member member = checkMemberValid(principal, "댓글 갱신 실패: 비로그인 사용자입니다.",
                "댓글 갱신 실패: 권한이 없습니다.");

        Comment comment = commentRepository.findByIdAndIsDeleteFalse(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글 갱신 실패: 해당 댓글이 존재하지 않습니다."));


        //댓글 작성자와 토큰 유저 정보가 다를 경우 처리
        if (!comment.getMember().equals(member)) {
            throw new NoAuthorizationException("댓글 갱신 실패: 권한이 없습니다.");
        }

        comment.update(req);


    }

    @Transactional
    public void deleteComment(Long commentId, CustomUserDetails principal) {
        //토큰에서
        Member member = checkMemberValid(principal, "댓글 삭제 실패: 비로그인 사용자입니다.",
                "댓글 삭제 실패: 권한이 없습니다.");

        Comment comment = commentRepository.findByIdAndIsDeleteFalse(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글 삭제 실패: 존재하지 않는 댓글입니다."));

        if (!comment.getMember().equals(member)) {
            throw new NoAuthorizationException("댓글 삭제 실패: 권한이 없습니다.");
        }


        comment.tempDelete();
        comment.getPost().decreaseCommentCnt();

    }

    private Member checkMemberValid(CustomUserDetails principal, String noLoginMessage, String notValidUserMessage) {
        if (principal == null) {
            throw new NoAuthorizationException(noLoginMessage);
        }

        Member author = memberRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new NoAuthorizationException(notValidUserMessage));

        if (author.getStatus() != MemberStatus.active) {
            throw new NoAuthorizationException(notValidUserMessage);
        }
        return author;
    }


}
