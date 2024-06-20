package sideproject.gugumo.repository;

import org.springframework.data.domain.Pageable;
import sideproject.gugumo.domain.dto.CmntDto;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.domain.entity.member.Member;

import java.util.List;

public interface CmntRepositoryCustom {
    List<CmntDto> findComment(Long postId, Member user);
}
