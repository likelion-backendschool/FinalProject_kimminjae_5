package com.example.reader.member.controller;

import com.example.DataNotFoundException;
import com.example.base.dto.RsData;
import com.example.member.MemberDto;
import com.example.member.MemberService;
import com.example.reader.member.LoginDto;
import com.example.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/member")
public class ReaderMemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    public ResponseEntity<RsData> login(@RequestBody LoginDto loginDto) {
        System.out.println(loginDto);
        if (loginDto.isNotValid()) {
            return Ut.spring.responseEntityOf(RsData.of("F-1", "로그인 정보가 올바르지 않습니다."));
        }
        MemberDto member;
        try {
            member = memberService.getMemberByUsername(loginDto.getUsername());
        } catch(DataNotFoundException e) {
            return Ut.spring.responseEntityOf(RsData.of("F-2", "일치하는 회원이 존재하지 않습니다."));
        }

        if (member == null) {
            return Ut.spring.responseEntityOf(RsData.of("F-2", "일치하는 회원이 존재하지 않습니다."));
        }

        if (passwordEncoder.matches(loginDto.getPassword(), member.getPassword()) == false) {
            return Ut.spring.responseEntityOf(RsData.of("F-3", "비밀번호가 일치하지 않습니다."));
        }

        String accessToken = memberService.genAccessToken(member.toEntity());

        return Ut.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "로그인 성공, Access Token을 발급합니다.",
                        Ut.mapOf(
                                "accessToken", accessToken
                        )
                ),
                Ut.spring.httpHeadersOf("Authentication", accessToken)
        );
    }
}
