package com.example.admin;

import com.example.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/adm")
@RequiredArgsConstructor
public class AdminController {
    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showIndex() {
        return "redirect:/adm/home/main";
    }

    @GetMapping("/home/main")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMain() {
        return "adm/home/main";
    }

}

