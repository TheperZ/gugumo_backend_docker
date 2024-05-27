package sideproject.gugumo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sideproject.gugumo.domain.dto.CommentDto;
import sideproject.gugumo.domain.dto.CustomUserDetails;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentDto> findComment(Long postId, CustomUserDetails principal, Pageable pageable);
}
