package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sideproject.gugumo.domain.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
