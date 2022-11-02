package com.example.withdraw;

import com.example.member.MemberDto;
import com.example.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/withdraw")
public class WithdrawController {
    private final WithdrawService withdrawService;
    private final MemberService memberService;

    @GetMapping("/apply")
    public String requestWithdraw(Principal principal, Model model) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());

        model.addAttribute("member", memberDto);
        return "/withdraw/apply_form";
    }
}
