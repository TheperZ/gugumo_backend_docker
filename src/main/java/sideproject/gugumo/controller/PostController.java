package sideproject.gugumo.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.dto.detailpostdto.DetailPostDto;
import sideproject.gugumo.dto.SimplePostDto;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.request.CreatePostReq;
import sideproject.gugumo.request.UpdatePostReq;
import sideproject.gugumo.service.PostService;


/**
 * /api/v1이 중복
 * 묶고 싶은데...
 * 커스텀 어노테이션: @RequestMapping("/meeting")과의 순서 보장? 애초에 실행은 되는가?
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meeting")
public class PostController {

    private final PostService postService;

    @PostMapping("/new")
    public ResponseEntity<String> save(/*@AuthenticationPrincipal CustomUserDeatils principal*/ @RequestBody CreatePostReq createPostReq) {
        postService.save(createPostReq);

        return ResponseEntity.status(201).body("글 작성 완료");
    }


    @GetMapping
    public ResponseEntity<Page<SimplePostDto>> findPostSimple(
            /*@AuthenticationPrincipal CustomUserDeatils principal*/
            @PageableDefault(size=12, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, value = "q") String q,
            @RequestParam(required = false, value="location") String location,
            @RequestParam(required = false, value="gametype") String gameType,
            @RequestParam(required = false, value="meetingstatus", defaultValue = "recruit") String meetingStatus,
            @RequestParam(required = false, value = "sorttype", defaultValue = "new") String sortType) {

        return ResponseEntity.ok(postService.findSimplePost(pageable, q, location, gameType, meetingStatus, sortType));
    }
    @GetMapping("/{post_id}")
    public <T extends DetailPostDto> ResponseEntity<T> findPostDetail(
            /*@AuthenticationPrincipal CustomUserDeatils principal*/
            @PathVariable("post_id") Long postId) {
        DetailPostDto detailPostDto = postService.findDetailPostByPostId(postId);

        return ResponseEntity.ok((T)detailPostDto);
    }

    @PatchMapping("/{post_id}")
    public ResponseEntity<String> updatePost(/*@AuthenticationPrincipal CustomUserDeatils principal*/
                                             @PathVariable("post_id") Long postId,
                                             @RequestBody UpdatePostReq updatePostReq) {
        postService.update(postId, updatePostReq);

        return ResponseEntity.ok("글 갱신 완료");
    }


    @DeleteMapping("/{post_id}")
    public ResponseEntity<String> deletePost(
            /*@AuthenticationPrincipal CustomUserDeatils principal*/
            @PathVariable("post_id") Long postId) {

        postService.deletePost(postId);

        return ResponseEntity.ok("글 삭제 완료");
    }

    /**
     * 테스트 코드: 추후 반드시 삭제할 것
     */
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("testuser", "testnick"));        //memberId: 1
    }
}
