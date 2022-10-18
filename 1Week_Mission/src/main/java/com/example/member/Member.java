package com.example.member;

import com.example.post.Post;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private int authLevel;

    @OneToMany(mappedBy = "member")
    private List<Post> postList;

    public MemberDto toDto() {
        MemberDto memberDto = MemberDto.builder()
                .id(id)
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String newPassword) {
            this.password = newPassword;
    }

    public void setAuthLevel(int authLevel) {
        this.authLevel = authLevel;
    }
}
