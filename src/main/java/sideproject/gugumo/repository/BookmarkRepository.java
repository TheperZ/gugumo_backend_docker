package sideproject.gugumo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.Bookmark;
import sideproject.gugumo.domain.Member;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    public Page<Bookmark> findByMember(Member member, Pageable pageable);
}
