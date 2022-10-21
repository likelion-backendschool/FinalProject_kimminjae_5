package com.example.member;

import com.example.DataNotFoundException;
import com.example.Util;
import com.example.hashTag.HashTagDto;
import com.example.hashTag.HashTagService;
import com.example.mail.MailService;
import com.example.mail.MailTO;
import com.example.post.PostDto;
import com.example.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final PostService postService;
    private final HashTagService hashTagService;

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
        //폼 에러 있을 경우
        if (bindingResult.hasErrors()) {
            return "member/join_form";
        }
        //비밀번호와 비밀번호 확인이 일치하지 않을 경우
        if (!memberForm.getPassword().equals(memberForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "member/join_form";
        }
        try {
            if (memberForm.getNickname() == null) {
                memberService.create(memberForm.getUsername(), memberForm.getPassword(), memberForm.getEmail());
            } else {
                memberService.create(memberForm.getUsername(), memberForm.getPassword(), memberForm.getNickname(), memberForm.getEmail());
            }
            //메일 생성 & 발송
//            MailTO mail = new MailTO(memberForm.getEmail(), "멋북스 회원가입 축하 메일", "멋북스의 회원이 되신 것을 진심으로 축하드립니다!");
//            mailService.sendMail(mail);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "member/join_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "member/join_form";
        }
        return "redirect:/";
    }

    //마이페이지, 프로필
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public String memberDetail(Model model, Principal principal, @RequestParam(value = "tag", defaultValue = "") String tag) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());
        List<PostDto> postDtoList;
        List<HashTagDto> tagDtoList = hashTagService.getHashTagByMember(memberDto);

        if(tag.length() == 0) {
            postDtoList = postService.getPostByMember(memberDto);
        } else {
            postDtoList = postService.getPostByTagAndMember(memberDto, tag);
        }

        model.addAttribute("tagList", tagDtoList);
        model.addAttribute("postList", postDtoList);
        model.addAttribute("member", memberDto);

        return "member/profile";
    }

    //작가로 등록
    @PostMapping("/signup/author")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String signupAuthor(@RequestParam("nickname") String nickname, Principal principal) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());
        try {
            memberService.signupAuthor(memberDto, nickname);
        } catch (DataIntegrityViolationException e) { //작가명이 중복될 경우
            String errorMessage = "<script>alert('이미 존재하는 작가명입니다.'); location.href='/member'</script>";
            return errorMessage;
        }
        return "<script>location.href='/member'</script>";
    }

    //회원 정보 수정
    @GetMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String modify(MemberModifyForm memberModifyForm, Principal principal) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());

        //수정 권한 검사
        if (!memberDto.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        memberModifyForm.setNickname(memberDto.getNickname());
        memberModifyForm.setEmail(memberDto.getEmail());
        return "member/modify_form";
    }

    //회원 정보 수정 처리
    @PostMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String modify(@Valid MemberModifyForm memberModifyForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "member/modify_form";
        }

        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());
        //수정권한 검사
        if (!memberDto.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        try {
            memberService.modify(memberDto, memberModifyForm.getEmail(), memberModifyForm.getNickname());
        } catch (DataIntegrityViolationException e) { //작가명 혹은 이메일이 중복될 경우
            e.printStackTrace();
            bindingResult.rejectValue("nickname", "alreadyExistData", "이미 존재하는 작가명 혹은 이메일입니다.");
            return "member/modify_form";
        }
        return "redirect:/member";
    }

    //비밀번호 변경
    @GetMapping("/modifyPassword")
    @PreAuthorize("isAuthenticated()")
    public String modifyPassword(PasswordForm passwordForm, Principal principal) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());

        //수정 권한 검사
        if (!memberDto.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        return "member/password_modify_form";
    }

    //비밀번호 변경 처리
    @PostMapping("/modifyPassword")
    @PreAuthorize("isAuthenticated()")
    public String modifyPassword(@Valid PasswordForm passwordForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "member/password_modify_form";
        }

        //비밀번호와 비밀번호 확인 비교
        if (!passwordForm.getPassword().equals(passwordForm.getPasswordConfirm())) {
            bindingResult.rejectValue("password", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "member/password_modify_form";
        }

        //기존 비밀번호 확인
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());
        String password = passwordForm.getOldPassword();
        if (!passwordEncoder.matches(password, memberDto.getPassword())) {
            bindingResult.rejectValue("oldPassword", "passwordInCorrect", "기존 비밀번호가 틀렸습니다.");
            return "member/password_modify_form";
        }
        memberService.modifyPassword(memberDto, passwordForm.getPassword());

//        return "<script>alert('비밀번호가 변경되었습니다.'); location.href='/member/logout';</script>";

        //비밀번호 변경완료시 로그아웃
        return "redirect:/member/logout";
    }

    //이메일로 아이디 찾기 처리
    @GetMapping("/findUsername")
    @ResponseBody
    public String findUsernameByEmail(@RequestParam("email") String email) {
        MemberDto memberDto = memberService.getMemberByEmail(email);

        return memberDto.getUsername();
    }

    //비밀번호 찾기 폼
    @GetMapping("/findPassword")
    public String findPassword() {
        return "member/find_password_form";
    }

    //비밀번호 찾기
    @PostMapping("/findPassword")
    @ResponseBody
    public String findPassword(@RequestParam("email") String email, @RequestParam("username") String username) {
        MemberDto member;
        try {
            //회원 찾기
            member = memberService.getMemberByUsername(username);

            if (!member.getEmail().equals(email)) {
                throw new DataNotFoundException("회원을 찾을 수 없습니다.");
            }
        } catch(Exception e) {
            return "<script>alert('아이디 혹은 이메일을 확인하세요.'); location.href='/member/findPassword';</script>";
        }
        //임시 비밀번호 생성
        String newPassword = Util.makeRandomPassword();

        //임시 비밀번호로 비밀번호 변겅
        memberService.modifyPassword(member, newPassword);

        //메일 생성 & 발송
        MailTO mail = new MailTO(email, "임시 비밀번호 발급", "회원님의 임시 비밀번호는 " + newPassword + " 입니다.");
        mailService.sendMail(mail);

        return "<script>alert('임시 비밀번호가 메일로 발송되었습니다'); location.href='/';</script>";
    }
}
