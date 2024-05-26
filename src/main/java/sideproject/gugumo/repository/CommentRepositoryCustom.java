package sideproject.gugumo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sideproject.gugumo.domain.dto.CommentDto;
import sideproject.gugumo.domain.dto.CustomUserDetails;

public interface CommentRepositoryCustom {
    Page<CommentDto> findComment(Long postId, CustomUserDetails principal, Pageable pageable);
}
