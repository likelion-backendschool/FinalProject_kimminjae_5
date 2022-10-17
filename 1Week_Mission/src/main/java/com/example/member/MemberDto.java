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
}
