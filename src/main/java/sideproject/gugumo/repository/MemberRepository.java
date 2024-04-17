package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
