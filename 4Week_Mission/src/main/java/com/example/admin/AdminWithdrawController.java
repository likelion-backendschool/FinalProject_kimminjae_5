package com.example.admin;

import com.example.member.MemberService;
import com.example.util.Ut;
import com.example.withdraw.Withdraw;
import com.example.withdraw.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/adm/withdraw")
@RequiredArgsConstructor
public class AdminWithdrawController {
    private final WithdrawService withdrawService;
    private final MemberService memberService;
    @GetMapping("/applyList")
    public String applyList(Model model) {
        List<Withdraw> withdrawList = withdrawService.getAll();

        model.addAttribute("withdrawList", withdrawList);
        return "/adm/withdraw/applyList";
    }
    @PostMapping("/{id}")
    public String withdrawApply(@PathVariable("id") long id) {
        Withdraw withdraw = withdrawService.getById(id);
        boolean result = withdrawService.withdrawDone(id);


        return "redirect:/adm/withdraw/applyList?msg=%s".formatted(Ut.url.encode("처리되었습니다!"));
    }
}
