package sideproject.gugumo.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sideproject.gugumo.domain.entity.Cmnt;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.post.Post;

@Getter
@AllArgsConstructor
public class CommentFcmEvent {


    private Cmnt cmnt;
    private Member cmntAuthor;

    public boolean isCmntPostAuthorEq(Post post) {
        return !cmntAuthor.getId().equals(post.getMember().getId());
    }

}
