package sideproject.gugumo.controller;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.cond.MeetingStatusType;
import sideproject.gugumo.cond.SortType;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.MemberRole;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.dto.CustomUserDetails;
import sideproject.gugumo.dto.detailpostdto.DetailPostDto;
import sideproject.gugumo.dto.simplepostdto.SimpleTransPostDto;
import sideproject.gugumo.page.PageCustom;
import sideproject.gugumo.repository.MemberRepository;
import sideproject.gugumo.request.CreatePostReq;
import sideproject.gugumo.request.UpdatePostReq;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.PostService;
import sideproject.gugumo.validate.EnumValue;


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
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> save(@AuthenticationPrincipal CustomUserDetails principal,
                                    @RequestBody @Valid CreatePostReq createPostReq) {
        postService.save(principal, createPostReq);

        return ApiResponse.createSuccess("글 작성 완료");
    }


    /**
     * 정렬(Sort)은 조건이 조금만 복잡해져도 Pageable의 Sort기능을 사용하기 어렵다. 루트 엔티티 범위를 넘어가는(join을 하는 등)
     * 동적 정렬 기능이 필요하면 스프링 데이터 페이징이 제공하는 Sort를 사용하기 보다는 파라미터를 받아서 직접 처리하는 것을 권장한다.
     */
    @GetMapping
    public <T extends SimpleTransPostDto> ApiResponse<PageCustom<T>> findPostSimple(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PageableDefault(size=12) Pageable pageable,
            @RequestParam(required = false, value = "q") String q,
            @RequestParam(required = false, value="location") String location,
            @RequestParam(required = false, value="gametype") String gameType,
            @RequestParam(required = false, value="meetingstatus", defaultValue = "RECRUIT") String meetingStatus,
            @RequestParam(required = false, value = "sort", defaultValue = "NEW") String sortType) {



        return ApiResponse.createSuccess(postService.findSimplePost(principal, pageable, q, gameType, location, meetingStatus, sortType));
    }
    @GetMapping("/{post_id}")
    public <T extends DetailPostDto> ApiResponse<T> findPostDetail(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable("post_id") Long postId) {
        DetailPostDto detailPostDto = postService.findDetailPostByPostId(principal, postId);

        return ApiResponse.createSuccess((T)detailPostDto);
    }

    @PatchMapping("/{post_id}")
    public ApiResponse<String> updatePost(@AuthenticationPrincipal CustomUserDetails principal,
                                             @PathVariable("post_id") Long postId,
                                             @RequestBody @Valid UpdatePostReq updatePostReq) {
        postService.update(principal, postId, updatePostReq);

        return ApiResponse.createSuccess("글 갱신 완료");
    }


    @DeleteMapping("/{post_id}")
    public ApiResponse<String> deletePost(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable("post_id") Long postId) {

        postService.deletePost(principal, postId);

        return ApiResponse.createSuccess("글 삭제 완료");
    }


    @GetMapping("/my")
    public <T extends SimpleTransPostDto> ApiResponse<PageCustom<T>> findMyPost(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PageableDefault(size=12, sort="createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.createSuccess(postService.findMyPost(principal, pageable));

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
