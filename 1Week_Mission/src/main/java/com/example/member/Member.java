package com.example.member;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private int authLevel;

    public MemberDto toDto() {
        MemberDto memberDto = MemberDto.builder()
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .email(this.email)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .authLevel(this.authLevel)
                .build();

        return memberDto;
    }
}
