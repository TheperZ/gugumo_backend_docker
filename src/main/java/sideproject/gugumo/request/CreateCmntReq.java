package sideproject.gugumo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateCmntReq {

    @NotNull
    private Long postId;
    @NotEmpty
    private String content;
    private Long parentCommentId;


}
