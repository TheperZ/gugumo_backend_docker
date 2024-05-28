package sideproject.gugumo.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.CommentDto;
import sideproject.gugumo.domain.dto.CustomUserDetails;
import sideproject.gugumo.page.PageCustom;
import sideproject.gugumo.request.CreateCommentReq;
import sideproject.gugumo.request.UpdateCommentReq;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> saveComment(@AuthenticationPrincipal CustomUserDetails principal,
                                           @Valid @RequestBody CreateCommentReq req) {
        commentService.save(req, principal);

        return ApiResponse.createSuccess("댓글 저장 완료");

    }

    @GetMapping("/{post_id}")
    public ApiResponse<List<CommentDto>> findComment(@AuthenticationPrincipal CustomUserDetails principal,
                                                     @PathVariable("post_id") Long postId,
                                                     @PageableDefault Pageable pageable) {

        return ApiResponse.createSuccess(commentService.findComment(postId, principal, pageable));
    }

    @PatchMapping("/{comment_id}")
    public ApiResponse<String> updateComment(@AuthenticationPrincipal CustomUserDetails principal,
                                             @PathVariable("comment_id") Long commentId,
                                             @RequestBody UpdateCommentReq req) {

        commentService.updateComment(commentId, req, principal);

        return ApiResponse.createSuccess("댓글 갱신 완료");

    }

    @DeleteMapping("/{comment_id}")
    public ApiResponse<String> deleteComment(@AuthenticationPrincipal CustomUserDetails principal,
                                             @PathVariable("comment_id") Long commentId) {
        commentService.deleteComment(commentId, principal);

        return ApiResponse.createSuccess("댓글 삭제 완료");

    }
}
