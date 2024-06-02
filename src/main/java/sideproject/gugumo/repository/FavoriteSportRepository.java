package sideproject.gugumo.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.entity.member.FavoriteSport;
import sideproject.gugumo.domain.entity.member.Member;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteSportRepository {
    private final EntityManager em;

    @Transactional
    public void save(FavoriteSport favoriteSport) {
        em.persist(favoriteSport);
    }
}
