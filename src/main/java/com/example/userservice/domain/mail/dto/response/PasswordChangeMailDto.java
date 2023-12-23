package com.example.userservice.domain.mail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeMailDto {
    //보내려는 사람의 email
    String email;
    //이름
    String name;
    //임시 비밀번호
    String temPassword;
}
