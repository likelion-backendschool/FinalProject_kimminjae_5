package com.example.member.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.member.MemberRole;
import com.example.member.enitty.Member;
import com.example.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> _member = memberRepository.findByusername(username);

        if (_member.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        Member member = _member.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (member.getAuthLevel() == 10) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
        }
        authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));

        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}