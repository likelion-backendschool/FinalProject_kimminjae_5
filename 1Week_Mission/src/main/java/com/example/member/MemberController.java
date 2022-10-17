package com.example.member;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    //로그인
    @GetMapping("/login")
    public String login() {
        return "member/login_form";
    }

    //회원가입 폼
    @GetMapping("/join")
    public String join(MemberForm memberForm) {
        return "member/join_form";
    }

    //회원가입 처리
    @PostMapping("/join")
    public String join(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "member/join_form";
        }
        if(!memberForm.getPassword().equals(memberForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "member/join_form";
        }
        try {
            if(memberForm.getNickname().isEmpty()) {
                memberService.create(memberForm.getUsername(), memberForm.getPassword(), memberForm.getEmail());
            } else {
                memberService.create(memberForm.getUsername(), memberForm.getPassword(), memberForm.getNickname(), memberForm.getEmail());
            }
        } catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "member/join_form";
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "member/join_form";
        }
        return "redirect:/";
    }
    //마이페이지, 프로필
    @GetMapping("")
    public String memberDetail(Model model, Principal principal) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());
        model.addAttribute("member", memberDto);

        return "member/profile";
    }

    //작가로 등록
    @PostMapping("/signup/author")
    public String signupAuthor(@RequestParam("nickname") String nickname, Principal principal) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());
        memberService.signupAuthor(memberDto, nickname);

        return "redirect:/member";
    }

    //회원 정보 수정
    @GetMapping("/modify")
    public String modify(MemberModifyForm memberModifyForm, Principal principal) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());

        if(!memberDto.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        memberModifyForm.setNickname(memberDto.getNickname());
        memberModifyForm.setEmail(memberDto.getEmail());
        return "member/modify_form";
    }

    //회원 정보 수정 처리
    @PostMapping("/modify")
    public String modify(@Valid MemberModifyForm memberModifyForm, BindingResult bindingResult,  Principal principal) {
        if(bindingResult.hasErrors()) {
            return "member/modify_form";
        }
        //작가명 중복여부 검사
        MemberDto otherMemberDto = memberService.getMemberByNickname(memberModifyForm.getNickname());
        if(otherMemberDto != null) {
            bindingResult.rejectValue("nickname", "alreadyExistNickname", "이미 존재하는 작가명입니다.");
            return "member/modify_form";
        }
        //이메일 중복여부 검사
        MemberDto otherMemberDto2 = memberService.getMemberByEmail(memberModifyForm.getEmail());
        if(otherMemberDto2 != null) {
            bindingResult.rejectValue("email", "alreadyExistEmail", "이미 존재하는 이메일입니다.");
            return "member/modify_form";
        }

        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());
        //수정권한 검사
        if(!memberDto.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        memberService.modify(memberDto, memberModifyForm.getEmail(), memberModifyForm.getNickname());
        return "redirect:/member";
    }
}
