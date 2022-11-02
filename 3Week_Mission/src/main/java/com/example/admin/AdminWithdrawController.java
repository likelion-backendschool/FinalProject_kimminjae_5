package com.example.admin;

import com.example.withdraw.Withdraw;
import com.example.withdraw.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/adm/withdraw")
@RequiredArgsConstructor
public class AdminWithdrawController {
    private final WithdrawService withdrawService;
    @GetMapping("/applyList")
    public String applyList(Model model) {
        List<Withdraw> withdrawList = withdrawService.getAll();

        model.addAttribute("withdrawList", withdrawList);
        return "/adm/withdraw/applyList";
    }
}
