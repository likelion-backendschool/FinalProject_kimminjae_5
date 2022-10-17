package com.example.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberModifyForm {
    @NotEmpty(message = "작가명은 필수항목입니다.")
    private String nickname;

    @NotEmpty(message = "이메일는 필수항목입니다.")
    @Email
    private String email;
}
