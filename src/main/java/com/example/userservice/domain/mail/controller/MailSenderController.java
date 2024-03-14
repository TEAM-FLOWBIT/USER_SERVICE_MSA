package com.example.userservice.domain.mail.controller;

import com.example.userservice.domain.mail.dto.request.SendMailDto;
import com.example.userservice.domain.mail.dto.request.VerificationMailDto;
import com.example.userservice.domain.mail.service.MailSenderService;
import com.example.userservice.global.common.CommonResDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@Slf4j
@RequestMapping("/api/v1/mail")
public class MailSenderController {

    private final MailSenderService mailSenderService;


    @Autowired
    public MailSenderController(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }
    @Operation(
            description = "이메일을 전송하는 API"
    )
    @PostMapping("")
    public void sendEail(@RequestBody SendMailDto sendMailDto) throws MessagingException {
        log.info("이메일 전송");
        mailSenderService.sendMail(sendMailDto);
    }

    @Operation(
            description = "이메일 랜덤번호 인증하는 API"
    )
    @PostMapping("/verify")
    public ResponseEntity<?> verifyRandomNumber(@RequestBody VerificationMailDto verificationMailDto) throws MessagingException {
        log.info("이메일 검증 및 임시 비밀번호 전송");

        mailSenderService.sendVerificationMail(verificationMailDto);

        return new ResponseEntity<>(new CommonResDto<>(1, "이메일 검증이 완료되었습니다.",""), HttpStatus.OK);

    }

}
