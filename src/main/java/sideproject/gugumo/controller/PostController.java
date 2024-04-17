package sideproject.gugumo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sideproject.gugumo.request.PostReq;
import sideproject.gugumo.service.PostService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/meeting")
@ApiCustom
public class PostController {

    private final PostService postService;

    @GetMapping("/new")
    public ResponseEntity<String> savePost(PostReq postReq) {
        postService.save(postReq);

        return ResponseEntity.status(201).body("글 작성 완료");
    }
}
