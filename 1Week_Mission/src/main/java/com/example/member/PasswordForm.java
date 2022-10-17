package com.example.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PasswordForm {
    @NotEmpty(message = "현재 비밀번호는 필수항목입니다.")
    private String oldPassword;

    @NotEmpty(message = "변경할 비밀번호는 필수항목입니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String passwordConfirm;

}
