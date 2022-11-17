package com.example.admin.controller;

import com.example.member.service.MemberService;
import com.example.util.Ut;
import com.example.withdraw.enitty.Withdraw;
import com.example.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/adm/withdraw")
@RequiredArgsConstructor
public class AdminWithdrawController {
    private final WithdrawService withdrawService;
    private final MemberService memberService;

    //출금 신청 데이터 리스트
    @GetMapping("/applyList")
    public String applyList(Model model) {
        List<Withdraw> withdrawList = withdrawService.getAll();

        model.addAttribute("withdrawList", withdrawList);
        return "/adm/withdraw/applyList";
    }

    //출금 신청 처리
    @PostMapping("/{id}")
    public String withdrawApply(@PathVariable("id") long id) {
        Withdraw withdraw = withdrawService.getById(id);
        boolean result = withdrawService.withdrawDone(id);

        if(!result) return "redirect:/adm/withdraw/applyList?errorMsg=%s".formatted(Ut.url.encode("신청이 취소되었습니다!"));

        return "redirect:/adm/withdraw/applyList?msg=%s".formatted(Ut.url.encode("처리되었습니다!"));
    }
}
