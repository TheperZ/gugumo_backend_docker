package sideproject.gugumo.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.MemberRole;
import sideproject.gugumo.dto.CustomUserDetails;
import sideproject.gugumo.dto.detailpostdto.DetailPostDto;
import sideproject.gugumo.dto.simplepostdto.SimplePostDto;
import sideproject.gugumo.dto.simplepostdto.SimpleTransPostDto;
import sideproject.gugumo.page.PageCustom;
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
    public ResponseEntity<String> save(@AuthenticationPrincipal CustomUserDetails principal, @RequestBody CreatePostReq createPostReq) {
        postService.save(principal, createPostReq);

        return ResponseEntity.status(201).body("글 작성 완료");
    }


    /**
     * 정렬(Sort)은 조건이 조금만 복잡해져도 Pageable의 Sort기능을 사용하기 어렵다. 루트 엔티티 범위를 넘어가는(join을 하는 등)
     * 동적 정렬 기능이 필요하면 스프링 데이터 페이징이 제공하는 Sort를 사용하기 보다는 파라미터를 받아서 직접 처리하는 것을 권장한다.
     */
    @GetMapping
    public ResponseEntity<PageCustom<SimplePostDto>> findPostSimple(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PageableDefault(size=12) Pageable pageable,
            @RequestParam(required = false, value = "q") String q,
            @RequestParam(required = false, value="location") String location,
            @RequestParam(required = false, value="gametype") String gameType,
            @RequestParam(required = false, value="meetingstatus", defaultValue = "RECRUIT") String meetingStatus,
            @RequestParam(required = false, value = "sort", defaultValue = "NEW") String sortType) {


        Page<SimplePostDto> result = postService.findSimplePost(principal, pageable, q, location, gameType, meetingStatus, sortType);
        return ResponseEntity.ok(new PageCustom<>(result.getContent(), result.getPageable(), result.getTotalElements()));
    }
    @GetMapping("/{post_id}")
    public <T extends DetailPostDto> ResponseEntity<T> findPostDetail(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable("post_id") Long postId) {
        DetailPostDto detailPostDto = postService.findDetailPostByPostId(principal, postId);

        return ResponseEntity.ok((T)detailPostDto);
    }

    @PatchMapping("/{post_id}")
    public ResponseEntity<String> updatePost(@AuthenticationPrincipal CustomUserDetails principal,
                                             @PathVariable("post_id") Long postId,
                                             @RequestBody UpdatePostReq updatePostReq) {
        postService.update(principal, postId, updatePostReq);

        return ResponseEntity.ok("글 갱신 완료");
    }


    @DeleteMapping("/{post_id}")
    public ResponseEntity<String> deletePost(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable("post_id") Long postId) {

        postService.deletePost(principal, postId);

        return ResponseEntity.ok("글 삭제 완료");
    }


    @GetMapping("/my")
    public ResponseEntity<PageCustom<Object>> findMyPost(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PageableDefault(size=12, sort="postId", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.findMyPost(principal, pageable));

    }



    /**
     * 테스트 코드: 추후 반드시 삭제할 것
     */
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("testuser", "testnick", passwordEncoder.encode("password"), MemberRole.ROLE_USER));        //memberId: 1
        memberRepository.save(new Member("testuser2", "testnick2", passwordEncoder.encode("password"), MemberRole.ROLE_USER));        //memberId: 2
    }

}
