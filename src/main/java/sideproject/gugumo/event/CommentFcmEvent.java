package sideproject.gugumo.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sideproject.gugumo.domain.entity.Cmnt;
import sideproject.gugumo.domain.entity.member.Member;

@Getter
@AllArgsConstructor
public class CommentFcmEvent {


    private Cmnt cmnt;
    private Member cmntAuthor;

}
