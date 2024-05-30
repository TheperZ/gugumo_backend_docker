package sideproject.gugumo.repository;

import org.springframework.data.domain.Pageable;
import sideproject.gugumo.domain.dto.CmntDto;
import sideproject.gugumo.domain.dto.CustomUserDetails;

import java.util.List;

public interface CmntRepositoryCustom {
    List<CmntDto> findComment(Long postId, CustomUserDetails principal, Pageable pageable);
}
