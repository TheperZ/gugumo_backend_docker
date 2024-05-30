package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.entity.Cmnt;

import java.util.Optional;

@Repository
public interface CmntRepository extends JpaRepository<Cmnt, Long>, CmntRepositoryCustom {

    public Optional<Cmnt> findByIdAndIsDeleteFalse(Long id);
}
