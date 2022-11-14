package com.example.member.enitty;

import com.example.member.dto.MemberDto;
import com.example.util.Ut;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIncludeProperties({"id", "username"})
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

    private long restCash;

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
                .restCash(this.restCash)
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

    public void setRestCash(long newRestCash) {
        this.restCash = newRestCash;
    }

    public Map<String, Object> getAccessTokenClaims() {
        return Ut.mapOf(
                "id", getId(),
                "createDate", getCreateDate(),
                "updateDate", getUpdateDate(),
                "username", getUsername(),
                "email", getEmail(),
                "authorities", getAuthorities()
        );
    }
    // 현재 회원이 가지고 있는 권한들을 List<GrantedAuthority> 형태로 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        return authorities;
    }
}
