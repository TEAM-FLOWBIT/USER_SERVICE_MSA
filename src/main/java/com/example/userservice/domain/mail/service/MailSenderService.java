package com.example.userservice.domain.mail.service;

import com.example.userservice.domain.mail.dto.request.SendMailDto;
import com.example.userservice.domain.mail.dto.request.VerificationMailDto;

import javax.mail.MessagingException;

public interface MailSenderService {
    //인증 번호 전송 메소드
    void sendMail(SendMailDto sendMailDto) throws MessagingException, MessagingException;

    void sendVerificationMail(VerificationMailDto verificationMailDto) throws MessagingException, MessagingException;

    //이건 일단 무시해주세요!
    //void sendFailMail(List<AcceptEmailDto> acceptEmailDto) throws MessagingException;

}
