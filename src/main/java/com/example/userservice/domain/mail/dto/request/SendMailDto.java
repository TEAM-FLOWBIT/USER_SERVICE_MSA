package com.example.userservice.domain.mail.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SendMailDto {
    //보내려는 사람의 email
    String email;
    String emailPurpose;
}
