package sideproject.gugumo.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.request.FcmTokenDto;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.FcmNotificationTokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FcmNotificationTokenController {

    private final FcmNotificationTokenService fcmNotificationTokenService;

    @PostMapping("/subscribe")
    public ApiResponse<String> subscribe(@AuthenticationPrincipal CustomUserDetails principal,
                                         @RequestBody FcmTokenDto fcmTokenDto) {
        fcmNotificationTokenService.subscribe(principal, fcmTokenDto);

        return ApiResponse.createSuccess("토큰 저장 완료");
    }
}
