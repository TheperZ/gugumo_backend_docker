package sideproject.gugumo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sideproject.gugumo.cond.PostSearchCondition;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.domain.dto.simplepostdto.SimplePostQueryDto;

public interface PostRepositoryCustom {
    Page<SimplePostQueryDto> search(PostSearchCondition cond, Pageable pageable, CustomUserDetails principal);
}
