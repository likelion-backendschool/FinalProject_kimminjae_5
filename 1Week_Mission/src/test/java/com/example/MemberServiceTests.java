package com.example;


import com.example.member.Member;
import com.example.member.MemberDto;
import com.example.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MemberServiceTests {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberService memberService;

    //회원가입 테스트
    @Test
    void create() {
        MemberDto member = memberService.create("min356812", "qwer1234@", "nickname", "min3568@naver.com");

        assertThat(member.getUsername()).isEqualTo("min356812");
    }

    //회원 조회 테스트
    @Test
    void select() {
        MemberDto member = memberService.getMemberById(1L);

        assertThat(member.getUsername()).isEqualTo("min356812");
    }

    //회원 정보 수정 테스트
    @Test
    void modify() {
        MemberDto member = memberService.getMemberById(1L);
        memberService.modify(member, "min356812@naver.com", "nickname");
        MemberDto modifiedMember = memberService.getMemberById(1L);

        assertThat(modifiedMember.getEmail()).isEqualTo("min356812@naver.com");
    }

    //비밀번호 변경 테스트
    @Test
    void modifyPassword() {
        MemberDto member = memberService.getMemberById(1L);

        memberService.modifyPassword(member, "newqwer1234@");
        MemberDto modifiedMember = memberService.getMemberById(1L);

        boolean b = passwordEncoder.matches("newqwer1234@", modifiedMember.getPassword());
        assertTrue(b);
    }
}
