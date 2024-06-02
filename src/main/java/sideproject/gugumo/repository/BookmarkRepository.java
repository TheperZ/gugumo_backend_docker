package sideproject.gugumo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.entity.Bookmark;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.post.Post;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    public Page<Bookmark> findByMemberAndPostTitleContains(Member member, Pageable pageable, String title);

    public Optional<Bookmark> findByMemberAndPost(Member member, Post post);

    public boolean existsByMemberAndPost(Member member, Post post);

    public Long countByPost(Post post);

    public void deleteByPost(Post post);


}
