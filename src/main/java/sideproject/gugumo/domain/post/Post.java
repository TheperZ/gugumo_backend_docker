package sideproject.gugumo.domain.post;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import sideproject.gugumo.domain.Member;
import sideproject.gugumo.request.UpdatePostReq;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;

    @Column(length = 10000)
    private String content;

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();
    @Builder.Default
    private long viewCount = 0;


    @Builder.Default
    private boolean isDelete = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void update(UpdatePostReq updatePostReq) {

        this.title = updatePostReq.getTitle();
        this.content = updatePostReq.getContent();


    }

    public void tempDelete() {
        this.isDelete = true;
    }
}
