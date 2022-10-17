package com.example;


import com.example.member.Member;
import com.example.member.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberServiceTests {
    @Autowired
    private MemberService memberService;

    //회원가입 테스트
    @Test
    void create() {
        Member member = memberService.create("min356812", "qwer1234@", "nickname", "min3568@naver.com");

        assertThat("min356812").isEqualTo(member.getUsername());
        assertThat("qwer1234@").isEqualTo(member.getPassword());
    }

    //회원 조회 테스트
    @Test
    void select() {
        MemberDto member = memberService.getMemberById(1L);

        assertThat("min356812").isEqualTo(member.getUsername());
        assertThat("qwer1234@").isEqualTo(member.getPassword());
    }

    //회원 정보 수정 테스트
    @Test
    void modify() {
        MemberDto member = memberService.getMemberById(1L);
        memberService.modify(member, "min356812@naver.com", "nickname");
        MemberDto modifiedMember = memberService.getMemberById(1L);

        assertThat("min356812@naver.com").isEqualTo(modifiedMember.getEmail());
    }

    //비밀번호 변경 테스트
    @Test
    void modifyPassword() {
        MemberDto member = memberService.getMemberById(1L);
        memberService.modifyPassword(member, "newqwer1234@");
        MemberDto modifiedMember = memberService.getMemberById(1L);

        assertThat("newqwer1234@").isEqualTo(modifiedMember.getPassword());
    }
}
