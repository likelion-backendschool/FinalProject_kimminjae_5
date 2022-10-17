package com.example.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //일반 회원 생성
    public MemberDto create(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .createDate(LocalDateTime.now())
                .authLevel(3)
                .build();
        memberRepository.save(member);
        return member.toDto();
    }
    //작가 회원 생성
    public MemberDto create(String username, String password, String nickname, String email) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .createDate(LocalDateTime.now())
                .authLevel(7)
                .build();
        memberRepository.save(member);
        return member.toDto();
    }

    public MemberDto getMemberById(long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(optionalMember.isPresent()) {
            return optionalMember.get().toDto();
        } else {
            return null;
        }
    }

    public void modify(MemberDto memberDto, String email, String nickname) {
        Optional<Member> optionalMember = memberRepository.findById(memberDto.getId());
        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setEmail(email);
            member.setNickname(nickname);
            memberRepository.save(member);
        } else return;
    }

    public void modifyPassword(MemberDto memberDto, String password) {
        Optional<Member> optionalMember = memberRepository.findById(memberDto.getId());
        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setPassword(password);
            memberRepository.save(member);
        } else return;
    }
}
