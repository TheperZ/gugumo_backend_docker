package sideproject.gugumo.event;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import sideproject.gugumo.domain.entity.Cmnt;
import sideproject.gugumo.domain.entity.CustomNoti;
import sideproject.gugumo.domain.entity.FcmNotificationToken;
import sideproject.gugumo.domain.entity.NotificationType;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.post.Post;
import sideproject.gugumo.repository.CustomNotiRepository;
import sideproject.gugumo.repository.FcmNotificationTokenRepository;
import sideproject.gugumo.repository.PostRepository;

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
        if (targetPost.isEmpty()) {
            return;
        }
        Post post = targetPost.get();
        Member postWriter = post.getMember();

        CustomNoti noti = CustomNoti.builder()
                .content(cmnt.getContent())
                .notificationType(NotificationType.COMMENT)
                .member(postWriter)
                .postId(post.getId())
                .senderNick(event.getCmntAuthor().getNickname())
                .build();


        String message = ms.getMessage(null, new Object[]{noti.getSenderNick(), post.getTitle(), noti.getContent()}, null);

        //List로 받아서 모든 토큰에 대해 보내도록 변경
        List<FcmNotificationToken> tempToken = fcmNotificationTokenRepository.findByMember(postWriter);
        if (tempToken.isEmpty()) {
            return;
        }

        //토큰 여러개 집어넣기->한 계정에서의 여러 디바이스 사용
        Message commentMessage = getCommentMessage(message, token.getToken());
        String response = FirebaseMessaging.getInstance().send(commentMessage);

        //에러 발생시?
/*
        //db에서 찾기
        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    // The order of responses corresponds to the order of the registration tokens.
                    failedTokens.add(registrationTokens.get(i));
                }
            }

        //db에서 삭제
        tokenrepo.delete();
*/

        log.info("Fcm Response: {}", response);



        customNotiRepository.save(noti);

    }


    private Message getCommentMessage(String message, String fcmToken){
        Notification notification = Notification.builder()
                .setTitle("회원님의 게시글에 댓글이 달렸습니다.")
                .setBody(message)
                .build();
        return Message.builder()
                .setNotification(notification)
                .setToken(fcmToken)
                .build();
    }
}
