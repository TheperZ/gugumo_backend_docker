package sideproject.gugumo.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import sideproject.gugumo.domain.dto.CommentDto;
import sideproject.gugumo.domain.dto.CustomUserDetails;
import sideproject.gugumo.domain.dto.QCommentDto;
import sideproject.gugumo.domain.entity.Member;
import sideproject.gugumo.domain.entity.MemberStatus;
import sideproject.gugumo.domain.entity.QMember;
import sideproject.gugumo.domain.entity.post.QPost;

import java.util.List;

import static sideproject.gugumo.domain.entity.QComment.comment;
import static sideproject.gugumo.domain.entity.QMember.member;
import static sideproject.gugumo.domain.entity.post.QPost.post;


public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;


    public CommentRepositoryImpl(EntityManager em, MemberRepository memberRepository) {
        this.queryFactory = new JPAQueryFactory(em);
        this.memberRepository = memberRepository;
    }


    @Override
    public List<CommentDto> findComment(Long postId, CustomUserDetails principal, Pageable pageable) {

        Member user =
                principal == null ?
                        null : memberRepository.findByUsername(principal.getUsername()).get();

        if (user != null && user.getStatus() != MemberStatus.active) {
            user = null;
        }


        //isYours, isAuthorExpired 추가
        List<CommentDto> result = queryFactory.select(new QCommentDto(
                        comment.id,
                        comment.member.nickname,
                        user != null ? comment.member.eq(user) : Expressions.FALSE,
                        comment.member.isNull().or(comment.member.status.eq(MemberStatus.delete)),
                        comment.content,
                        comment.createDate,
                        comment.isNotRoot,
                        comment.parentComment.id,
                        comment.orderNum
                ))
                .from(comment)
                .join(comment.post, post)
                .leftJoin(comment.member, member)
                .where(
                        comment.post.id.eq(postId), comment.isDelete.isFalse()
                )
                .orderBy(comment.orderNum.asc(), comment.createDate.asc())
                .fetch();

        return result;
    }
}
