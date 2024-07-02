package sideproject.gugumo.event;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import sideproject.gugumo.domain.entity.Cmnt;
import sideproject.gugumo.domain.entity.notification.CustomNoti;
import sideproject.gugumo.domain.entity.notification.FcmNotificationToken;
import sideproject.gugumo.domain.entity.notification.NotificationType;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.post.Post;
import sideproject.gugumo.repository.CustomNotiRepository;
import sideproject.gugumo.repository.FcmNotificationTokenRepository;
import sideproject.gugumo.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener {

    private final PostRepository postRepository;
    private final FcmNotificationTokenRepository fcmNotificationTokenRepository;
    private final CustomNotiRepository customNotiRepository;
    private final MessageSource ms;

    @Async
    @TransactionalEventListener
    public void sendPostWriter(CommentFcmEvent event) throws FirebaseMessagingException {
        Cmnt cmnt = event.getCmnt();
        Optional<Post> targetPost = postRepository.findById(cmnt.getPost().getId());
        if (targetPost.isEmpty() || !event.isCmntPostAuthorEq(targetPost.get())) {
            return;
        }
        Post post = targetPost.get();
        Member postWriter = post.getMember();

        String message = cmnt.getContent();

        CustomNoti noti = CustomNoti.builder()
                .message(message)
                .notificationType(NotificationType.COMMENT)
                .member(postWriter)
                .postId(post.getId())
                .build();



        //List로 받아서 모든 토큰에 대해 보내도록 변경
        List<FcmNotificationToken> tempToken = fcmNotificationTokenRepository.findByMember(postWriter);
        if (tempToken.isEmpty()) {
            return;
        }

        List<String> tokens = new ArrayList<>();
        for (FcmNotificationToken fcmNotificationToken : tempToken) {
            tokens.add(fcmNotificationToken.getToken());
        }


        //토큰 여러개 집어넣기->한 계정에서의 여러 디바이스 사용
        MulticastMessage commentMessage = getCommentMessage(message, tokens, post.getId());
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(commentMessage);

        //에러 발생시?
        //db에서 찾기
        List<String> failedTokens = new ArrayList<>();
        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    // The order of responses corresponds to the order of the registration tokens.
                    failedTokens.add(tokens.get(i));
                }
            }
        }

        //db에서 삭제
        for (String failedToken : failedTokens) {
            fcmNotificationTokenRepository.deleteAllByToken(failedToken);
        }





        customNotiRepository.save(noti);

    }


    private MulticastMessage getCommentMessage(String message, List<String> tokens, Long postId){
        String title = ms.getMessage("push.comment.title",null,null);

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(message)
                .build();
        return MulticastMessage.builder()
                .setNotification(notification)
                .addAllTokens(tokens)
                .putData("postId", String.valueOf(postId))
                .build();
    }
}
