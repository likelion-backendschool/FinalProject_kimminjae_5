package com.example.config.entity;

import com.example.member.enitty.Member;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@JsonIncludeProperties({"id", "createDate", "updateDate", "username", "email"})
public class MemberContext extends User {
    private final long id;
    private final LocalDateTime createDate;
    private final LocalDateTime updateDate;
    private final String username;
    private final String email;
    private final Set<GrantedAuthority> authorities;

    public MemberContext(Member member) {
        super(member.getUsername(), "", member.getAuthorities());

        id = member.getId();
        createDate = member.getCreateDate();
        updateDate = member.getUpdateDate();
        username = member.getUsername();
        email = member.getEmail();
        authorities = member.getAuthorities().stream().collect(Collectors.toSet());
    }
}
