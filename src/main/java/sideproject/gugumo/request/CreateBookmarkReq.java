package sideproject.gugumo.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateBookmarkReq {

    @NotNull
    private Long postId;
}
