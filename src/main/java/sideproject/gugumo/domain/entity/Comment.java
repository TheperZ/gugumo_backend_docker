package sideproject.gugumo.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sideproject.gugumo.domain.entity.post.Post;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(length = 1000)
    private String content;


    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    //부모 댓글이 삭제되었을 경우->parentComment==null->얘가 부모 댓글이라 생각될 수 있음
    //위 이유에 의해 필요한 변수
    private boolean isNotRoot;

    //댓글의 순서를 보장하기 위해 사용되는 변수, 조상 댓글은 게시글의 전체 댓글 수, 손자 댓글은 조상의 변수를 물려받음
    //댓글 조회 시 얘를 기준으로 정렬할 예정
    private long orderNum;

    private boolean isDelete;


    public void tempDelete() {
        this.isDelete = true;
    }

    @Builder
    public Comment(Post post, Comment parentComment, String content, Member member) {
        this.content = content;
        this.post = post;
        this.parentComment = parentComment;
        this.member = member;
        if (parentComment == null) {
            this.isNotRoot = false;
            this.orderNum = post.getCommentCnt();
        } else {
            this.isNotRoot = true;
            this.orderNum = parentComment.getOrderNum();

        }
        this.createDate = LocalDateTime.now();
        this.isDelete = false;
    }
}
