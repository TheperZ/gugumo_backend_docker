package sideproject.gugumo.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sideproject.gugumo.domain.dto.CustomUserDetails;
import sideproject.gugumo.request.CreateCommentReq;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.CommentService;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/new")
    public ApiResponse<String> saveComment(@AuthenticationPrincipal CustomUserDetails principal,
                                           @Valid @RequestBody CreateCommentReq req) {
        commentService.save(req, principal);

        return ApiResponse.createSuccess("댓글 저장 완료");

    }
}
