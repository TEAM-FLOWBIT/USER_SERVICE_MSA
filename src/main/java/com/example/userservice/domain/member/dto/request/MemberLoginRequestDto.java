package com.example.userservice.domain.member.dto.request;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberLoginRequestDto {

    @NotEmpty(message = "아이디 설정은 필수입니다.")
    @Size(min = 4, max = 32, message = "아이디를 4~32글자로 설정해주세요.")
    private String userId;

    @NotEmpty(message = "비밀번호 설정은 필수입니다.")
    @Size(min = 8, max = 64, message = "비밀번호를 8~64글자의 영문+숫자 조합으로 설정해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$", message = "비밀번호는 영문과 숫자의 조합이어야 합니다.")
    private String password;
}
