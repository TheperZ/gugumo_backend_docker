package sideproject.gugumo.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sideproject.gugumo.domain.dto.EmailRequestDto;
import sideproject.gugumo.service.MailSenderService;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailSenderService mailSenderService;

    @PostMapping("/mailSend")
    public String mailSend(@RequestBody @Valid EmailRequestDto emailDto) {
        System.out.println("인증 이메일 : " + emailDto.getEmail());

        return mailSenderService.joinEmail(emailDto.getEmail());
    }
}
