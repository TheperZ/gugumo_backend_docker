package sideproject.gugumo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sideproject.gugumo.cond.PostSearchCondition;
import sideproject.gugumo.dto.CustomUserDetails;
import sideproject.gugumo.dto.simplepostdto.SimplePostDto;

public interface PostRepositoryCustom {
    Page<SimplePostDto> search(PostSearchCondition cond, Pageable pageable, CustomUserDetails principal);
}
