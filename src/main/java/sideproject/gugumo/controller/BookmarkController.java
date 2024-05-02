package sideproject.gugumo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.Bookmark;
import sideproject.gugumo.dto.BookmarkPostDto;
import sideproject.gugumo.dto.SimplePostDto;
import sideproject.gugumo.page.PageCustom;
import sideproject.gugumo.request.CreateBookmarkReq;
import sideproject.gugumo.service.BookmarkService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;


    @PostMapping("/new")
    public ResponseEntity<String> saveBookmark(@RequestBody CreateBookmarkReq createBookmarkReq) {

        bookmarkService.save(createBookmarkReq);

        return ResponseEntity.status(201).body("북마크 생성 완료");
    }

    @GetMapping
    public PageCustom<BookmarkPostDto> findBookmark(
            /*@AuthenticationPrincipal CustomUserDeatils principal*/
            @PageableDefault(size = 12) Pageable pageable) {

        return bookmarkService.findBookmarkByMember(pageable);
    }

    @DeleteMapping("/{bookmark_id}")
    public ResponseEntity<String> deleteBookmark(@PathVariable("bookmark_id") Long bookmarkId) {
        bookmarkService.delete(bookmarkId);

        return ResponseEntity.ok("북마크 삭제 완료");
    }

}
