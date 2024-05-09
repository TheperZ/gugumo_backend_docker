package sideproject.gugumo.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.dto.BookmarkPostDto;
import sideproject.gugumo.dto.CustomUserDetails;
import sideproject.gugumo.dto.simplepostdto.SimpleTransPostDto;
import sideproject.gugumo.page.PageCustom;
import sideproject.gugumo.request.CreateBookmarkReq;
import sideproject.gugumo.service.BookmarkService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;


    @PostMapping("/new")
    public ResponseEntity<String> saveBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody @Valid CreateBookmarkReq createBookmarkReq) {

        bookmarkService.save(principal, createBookmarkReq);

        return ResponseEntity.status(201).body("북마크 생성 완료");
    }

    @GetMapping
    public <T extends SimpleTransPostDto> PageCustom<T> findBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PageableDefault(size = 12) Pageable pageable) {

        return bookmarkService.findBookmarkByMember(principal, pageable);
    }

    @DeleteMapping("/{bookmark_id}")
    public ResponseEntity<String> deleteBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable("bookmark_id") Long bookmarkId) {
        bookmarkService.delete(principal, bookmarkId);

        return ResponseEntity.ok("북마크 삭제 완료");
    }

}
