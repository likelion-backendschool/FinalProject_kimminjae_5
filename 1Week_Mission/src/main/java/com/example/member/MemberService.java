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
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .email(email)
                .createDate(LocalDateTime.now())
                .authLevel(7)
                .build();
        memberRepository.save(member);
        return member.toDto();
    }

    //id로 회원 찾기
    public MemberDto getMemberById(long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(optionalMember.isPresent()) {
            return optionalMember.get().toDto();
        } else {
            return null;
        }
    }

    //회원 정보(이메일, 닉네임) 수정
    public void modify(MemberDto memberDto, String email, String nickname) {
        Optional<Member> optionalMember = memberRepository.findById(memberDto.getId());
        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setEmail(email);
            member.setNickname(nickname);
            memberRepository.save(member);
        } else return;
    }

    //비밀번호 변경
    public void modifyPassword(MemberDto memberDto, String password) {
        Optional<Member> optionalMember = memberRepository.findById(memberDto.getId());
        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setPassword(passwordEncoder.encode(password));
            memberRepository.save(member);
        } else return;
    }

    //username으로 Member찾기
    public MemberDto getMemberByUsername(String name) {
        Optional<Member> optionalMember = memberRepository.findByusername(name);
        if(optionalMember.isPresent()) {
            return optionalMember.get().toDto();
        } else {
            return null;
        }
    }

    //작가 등록
    public void signupAuthor(MemberDto memberDto, String nickname) {
        Optional<Member> optionalMember = memberRepository.findById(memberDto.getId());
        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setNickname(nickname);
            member.setAuthLevel(7);
            memberRepository.save(member);
        } else {
            return;
        }
    }

    //작가명으로 유저 찾기
    public MemberDto getMemberByNickname(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        if(optionalMember.isPresent()) {
            return optionalMember.get().toDto();
        } else {
            return null;
        }
    }

    //이메일로 유저 찾기
    public MemberDto getMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByNickname(email);
        if(optionalMember.isPresent()) {
            return optionalMember.get().toDto();
        } else {
            return null;
        }
    }
}
