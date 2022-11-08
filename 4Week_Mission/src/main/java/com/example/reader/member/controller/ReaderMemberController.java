package com.example.reader.member.controller;

import com.example.DataNotFoundException;
import com.example.base.dto.RsData;
import com.example.config.entity.MemberContext;
import com.example.member.MemberDto;
import com.example.member.MemberService;
import com.example.reader.member.dto.LoginDto;
import com.example.util.Ut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/member")
@Tag(name = "ApiV1MemberController", description = "로그인, 회원 정보")
public class ReaderMemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    @Operation(summary =  "로그인", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RsData> login(@RequestBody LoginDto loginDto) {
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
    @GetMapping("/me")
    @Operation(summary =  "회원 정보", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RsData> me(@AuthenticationPrincipal MemberContext memberContext) {
        System.out.println(memberContext);
        if (memberContext == null) { // 임시코드, 나중에는 시프링 시큐리티를 이용해서 로그인을 안했다면, 아예 여기로 못 들어오도록
            return Ut.spring.responseEntityOf(RsData.failOf(null));
        }

        return Ut.spring.responseEntityOf(RsData.successOf(Ut.mapOf("member", memberContext)));
//        return Ut.spring.responseEntityOf(RsData.successMemberOf(memberContext));
    }
}
