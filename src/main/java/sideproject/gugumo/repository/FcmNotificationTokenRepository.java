package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.entity.notification.FcmNotificationToken;
import sideproject.gugumo.domain.entity.member.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FcmNotificationTokenRepository extends JpaRepository<FcmNotificationToken, Long> {


    public boolean existsByMemberAndToken(Member member, String token);

    public Optional<FcmNotificationToken> findByMemberAndToken(Member member, String token);

    public void deleteAllByToken(String token);

    public List<FcmNotificationToken> findByMember(Member member);

    public List<FcmNotificationToken> findByCreateDateBefore(LocalDateTime expire);

}
