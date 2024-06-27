package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.entity.CustomNoti;
import sideproject.gugumo.domain.entity.member.Member;

import java.util.List;

@Repository
public interface CustomNotiRepository extends JpaRepository<CustomNoti, Long> {
    public List<CustomNoti> findByMemberOrderByCreateDateDesc(Member member);
}
