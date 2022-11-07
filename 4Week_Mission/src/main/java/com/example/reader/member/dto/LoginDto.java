package com.example.reader.member.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDto {
    @NotEmpty(message = "username 을(를) 입력해주세요.")
    private String username;

    @NotEmpty(message = "username 을(를) 입력해주세요.")
    private String password;
}
