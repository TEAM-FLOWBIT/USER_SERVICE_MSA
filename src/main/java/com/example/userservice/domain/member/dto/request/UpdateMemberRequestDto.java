package com.example.userservice.domain.member.dto.request;


import io.github.resilience4j.core.lang.Nullable;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMemberRequestDto {
    private String name;
    private String password;
    private String phoneNumber;
    @Nullable
    MultipartFile profileFile;
}


