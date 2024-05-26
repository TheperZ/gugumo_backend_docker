package sideproject.gugumo.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sideproject.gugumo.domain.dto.EmailCheckDto;
import sideproject.gugumo.domain.dto.EmailRequestDto;
import sideproject.gugumo.service.MailSenderService;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailSenderService mailService;

    @PostMapping("/mailSend")
    public String mailSend(@RequestBody @Valid EmailRequestDto emailDto) {
        System.out.println("인증 이메일 : " + emailDto.getEmail());

        mailService.joinEmail(emailDto.getEmail());

        return "인증번호 전송 성공";
    }

    @PostMapping("/mailAuthCheck")
    public String AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto) {

        boolean checked = mailService.checkAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());

        if(checked) {
            return "ok";
        }
        else {
            throw new NullPointerException("이메일 인증 에러");
        }
    }
}
