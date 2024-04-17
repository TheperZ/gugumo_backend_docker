package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.post.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
