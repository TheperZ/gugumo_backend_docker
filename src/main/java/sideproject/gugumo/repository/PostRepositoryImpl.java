package sideproject.gugumo.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import sideproject.gugumo.cond.PostSearchCondition;
import sideproject.gugumo.cond.SortType;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.domain.meeting.GameType;
import sideproject.gugumo.domain.meeting.Location;
import sideproject.gugumo.domain.meeting.MeetingStatus;
import sideproject.gugumo.dto.CustomUserDetails;
import sideproject.gugumo.dto.QSimplePostDto;
import sideproject.gugumo.dto.SimplePostDto;

import java.util.List;
import java.util.Optional;

import static sideproject.gugumo.domain.QBookmark.bookmark;
import static sideproject.gugumo.domain.meeting.QMeeting.meeting;
import static sideproject.gugumo.domain.post.QPost.*;

/**
 * querydsl을 이용한 동적 검색
 */
public class PostRepositoryImpl implements PostRepositoryCustom{


    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;


    public PostRepositoryImpl(EntityManager em, MemberRepository memberRepository) {
        this.queryFactory = new JPAQueryFactory(em);
        this.memberRepository = memberRepository;
    }


    @Override
    public Page<SimplePostDto> search(PostSearchCondition cond, Pageable pageable
            , CustomUserDetails principal) {

        Member member =
                principal == null ?
                        null : memberRepository.findByUsername(principal.getUsername()).get();

        OrderSpecifier orderSpecifier = createOrderSpecifier(cond.getSortType());

        List<SimplePostDto> result = queryFactory.select(new QSimplePostDto(
                        post.id.as("postId"),
                        meeting.status,
                        meeting.gameType,
                        meeting.location,
                        post.title,
                        meeting.meetingDateTime,
                        meeting.meetingDays,
                        meeting.meetingMemberNum,
                        meeting.meetingDeadline,
                        bookmark.isNotNull().as("isBookmarked")
                ))
                .from(post)
                .leftJoin(post.meeting, meeting)
                .leftJoin(bookmark).on(bookmark.post.eq(post), hasMember(member))
                .where(
                        queryEq(cond.getQ()), locationEq(cond.getLocation()),
                        gameTypeEq(cond.getGameType()), meetingStatusEq(cond.getMeetingStatus()),
                        post.isDelete.isFalse()
                )
                .orderBy(orderSpecifier, post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> count = queryFactory.select(post.count())
                .from(post)
                .leftJoin(post.meeting, meeting)
                .where(queryEq(cond.getQ()), locationEq(cond.getLocation()),
                        gameTypeEq(cond.getGameType()), meetingStatusEq(cond.getMeetingStatus()));


        return PageableExecutionUtils.getPage(result, pageable, count::fetchOne);

    }


    private OrderSpecifier createOrderSpecifier(SortType sortType) {
        return switch (sortType) {
            case OLD->new OrderSpecifier<>(Order.ASC, post.createDate);
            case LIKE-> new OrderSpecifier<>(Order.DESC, post.viewCount);
            default -> new OrderSpecifier<>(Order.DESC, post.createDate);
        };
    }

    private BooleanExpression hasMember(Member member) {
        return member != null ? bookmark.member.eq(member) : Expressions.TRUE;
    }

    private BooleanExpression meetingStatusEq(MeetingStatus meetingStatus) {
        return meetingStatus != null ? meeting.status.eq(meetingStatus) : null;
    }


    private BooleanExpression gameTypeEq(GameType gameType) {
        return gameType != null ? meeting.gameType.eq(gameType) : null;
    }

    private BooleanExpression locationEq(Location location) {
        return location != null ? meeting.location.eq(location) : null;
    }

    /**
     * contains("string"): '%string%'으로 나감
     * like("string"): 'string'으로 나감->%나 _ 등을 쓰려면 붙여줘야 함
     */
    private BooleanExpression queryEq(String q) {
        return q != null ? post.title.contains(q) : null;
    }
}
