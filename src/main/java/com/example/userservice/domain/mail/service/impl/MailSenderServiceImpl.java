package com.example.userservice.domain.mail.service.impl;

import com.example.userservice.domain.mail.MailPurpose;
import com.example.userservice.domain.mail.dto.request.SendMailDto;
import com.example.userservice.domain.mail.dto.request.VerificationMailDto;
import com.example.userservice.domain.mail.service.MailSenderService;
import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.domain.member.repository.MemberRepository;
import com.example.userservice.global.config.redis.util.EmailRedisUtil;
import com.example.userservice.global.exception.error.DuplicateAccountException;
import com.example.userservice.global.exception.error.EmailNotValidException;
import com.example.userservice.global.exception.error.NotFoundAccountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailRedisUtil emailRedisUtil;
    private final MemberRepository memberRepository;




    @Override
    public void sendMail(SendMailDto sendMailDto) throws MessagingException {
        String email=sendMailDto.getEmail();
        Integer randomNumber=getVerificationNumber();
        String verifyPurpose = sendMailDto.getEmailPurpose();

        // state 별 이메일 유효성 검증
        emailValidationCheck(email, verifyPurpose);

        if (emailRedisUtil.existData(email)){
            emailRedisUtil.deleteData(email);
        }


        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("플로우 빗 인증메일");

        //템플릿에 전달할 데이터 설정
        Context context = new Context();
        context.setVariable("randomNumber", randomNumber);

        //메일 내용 설정 : 템플릿 프로세스
        String html = templateEngine.process("acceptEmail.html",context);
        helper.setText(html, true);
        //메일 보내기
        javaMailSender.send(message);

        // 기존에 데이터가 있다면
        if(emailRedisUtil.existData(email)){
            emailRedisUtil.deleteData(email);
            emailRedisUtil.setListData(email,randomNumber,verifyPurpose,60*3L);
        }else{
            emailRedisUtil.setListData(email,randomNumber,verifyPurpose,60*3L);
        }


    }

    @Override
    @Transactional
    public void sendVerificationMail(VerificationMailDto verificationMailDto) throws MessagingException {
        String userInputCode=verificationMailDto.getRandomNumber();
        String email = verificationMailDto.getEmail();
        String verifyPurpose = verificationMailDto.getEmailPurpose();

        List<String> info = emailRedisUtil.getData(email);
        String[] splitInfo = info.get(0).split("\\|");
        String verificationCode = splitInfo[0];
        String redisVerifyPurpose = splitInfo[1];
        if(info.isEmpty() || !Objects.equals(verificationCode, verificationMailDto.getRandomNumber()) || !Objects.equals(verifyPurpose, redisVerifyPurpose)){
            throw new EmailNotValidException("인증이 유효하지 않습니다");
        }

        if(Objects.equals(verifyPurpose, MailPurpose.SIGNUP.toString()) && userInputCode.equals(verificationCode)){
            emailRedisUtil.setListData(email,0,"signupVerifySuccess",60*20L);
        }
    }

    private void emailValidationCheck(String email, String verifyPurpose) {
        if(verifyPurpose.equals("FORGOT_PASSWORD")){
            memberRepository.findByUserId(email).orElseThrow(NotFoundAccountException::new);
        }
        if(verifyPurpose.equals("SIGNUP")){
            Optional<Member> member = memberRepository.findByUserId(email);
            if(member.isPresent()){
                throw new DuplicateAccountException("중복된 회원입니다");
            }

        }
    }

    private Integer getVerificationNumber() {
        // 난수의 범위 111111 ~ 999999 (6자리 난수)
        Random r = new Random();
        Integer checkNum = r.nextInt(888888) + 111111;

        return checkNum;
    }
}
