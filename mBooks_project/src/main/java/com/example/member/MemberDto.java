package com.example.member;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private int authLevel;

    private long restCash;

    public Member toEntity() {
        return Member.builder()
                .id(this.id)
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .email(this.email)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .authLevel(this.authLevel)
                .restCash(this.restCash)
                .build();
    }
}
