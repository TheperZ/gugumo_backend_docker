package sideproject.gugumo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.request.CreatePostReq;
import sideproject.gugumo.request.UpdatePostReq;
import sideproject.gugumo.service.PostService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/meeting")
@ApiCustom
public class PostController {

    private final PostService postService;

    @GetMapping("/new")
    public ResponseEntity<String> savePost(@RequestBody CreatePostReq createPostReq) {
        postService.save(createPostReq);

        return ResponseEntity.status(201).body("글 작성 완료");
    }

    @PatchMapping("/{post_id}")
    public ResponseEntity<String> updatePost(@PathVariable("post_id") Long postId, @RequestBody UpdatePostReq updatePostReq) {
        postService.update(postId, updatePostReq);

        return ResponseEntity.ok("글 갱신 완료");
    }

    /**
     * 현재 프로그램은 데이터를 완전히 지우는 것이 아닌 isDelete의 값만 변경하도록 구현
     * 따라서 DeleteMapping이 아닌 PostMapping을 사용
     * PatchMapping은 글쎄...
     */
    @PostMapping("/{post_id}")
    public ResponseEntity<String> deletePost(@PathVariable("post_id") Long postId) {

        postService.deletePost(postId);

        return ResponseEntity.ok("글 삭제 완료");
    }
}
