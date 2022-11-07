package com.example.reader.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDto {
    @NotEmpty(message = "username 을(를) 입력해주세요.")
    private String username;

    @NotEmpty(message = "username 을(를) 입력해주세요.")
    private String password;
    public boolean isNotValid() {
        return username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0;
    }
}
