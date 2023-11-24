package com.example.userservice.dto;

import com.example.userservice.VO.ResponseOrder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userID;
    private Date createdAt;

    private String encryptedPwd;

    private List<ResponseOrder> orders;
}
