package sideproject.gugumo.domain.entity.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sideproject.gugumo.domain.entity.meeting.GameType;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FavoriteSport {

    @Id
    @GeneratedValue
    @Column(name = "favorite_sport_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private GameType gameType;

    public static FavoriteSport createFavoriteSport(String sport, Member member) {
        FavoriteSport favoriteSport = new FavoriteSport();

        favoriteSport.gameType = GameType.valueOf(sport);
        favoriteSport.member = member;

        return favoriteSport;
    }
}