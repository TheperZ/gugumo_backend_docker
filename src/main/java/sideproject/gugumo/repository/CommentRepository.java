package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sideproject.gugumo.domain.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
