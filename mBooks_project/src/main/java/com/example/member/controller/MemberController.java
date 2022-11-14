package com.example.member.controller;

import com.example.DataNotFoundException;
import com.example.member.form.MemberForm;
import com.example.member.form.MemberModifyForm;
import com.example.member.service.MemberService;
import com.example.member.form.PasswordForm;
import com.example.member.dto.MemberDto;
import com.example.util.Util;
import com.example.mybook.entity.MyBook;
import com.example.mybook.service.MyBookService;
import com.example.order.entity.Order;
import com.example.order.service.OrderService;
import com.example.post.post_hashTag.HashTagService;
import com.example.mail.MailService;
import com.example.mail.MailTO;
import com.example.post.dto.PostDto;
import com.example.post.service.PostService;
import com.example.product.dto.ProductDto;
import com.example.product.service.ProductService;
import com.example.product.product_hashTag.ProductHashTagService;
import com.example.util.Ut;
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
    private final ProductService productService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final PostService postService;
    private final HashTagService hashTagService;
    private final ProductHashTagService productHashTagService;
    private final OrderService orderService;
    private final MyBookService myBookService;

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
            if (memberForm.getNickname().length() == 0) {
                memberService.create(memberForm.getUsername(), memberForm.getPassword(), memberForm.getEmail());
            } else if(memberForm.getUsername().equals("admin")) {
                memberService.createAdmin(memberForm.getUsername(), memberForm.getPassword(), memberForm.getEmail(), memberForm.getNickname());
            } else {
                memberService.create(memberForm.getUsername(), memberForm.getPassword(), memberForm.getNickname(), memberForm.getEmail());
            }
            //메일 생성 & 발송
            MailTO mail = new MailTO(memberForm.getEmail(), "멋북스 회원가입 축하 메일", "멋북스의 회원이 되신 것을 진심으로 축하드립니다!");
            mailService.sendMail(mail);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "member/join_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "member/join_form";
        }
        return "redirect:/member/login?msg=%s".formatted(Ut.url.encode("회원가입이 완료되었습니다!"));
    }

    //마이페이지, 프로필
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public String memberDetail(Model model, Principal principal, @RequestParam(value = "listType", defaultValue = "") String listType, @RequestParam(value = "tag", defaultValue = "") String tag) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());

        List<ProductDto> productDtos = null;
        List<PostDto> postDtoList = null;
        List<String> tagDtoList = null;
        List<String> productHashTagDtos = null;
        List<Order> orderList = null;
        List<MyBook> myBookList = null;

        if(listType.equals("product") || listType.equals("")) {
            productHashTagDtos = productHashTagService.getKeywordContent(memberDto);
            if(tag.length() == 0) {
                productDtos = productService.getByMember(memberDto);
            } else {
                productDtos = productService.getProductByTagAndMember(memberDto, tag);
            }
        } else if(listType.equals("post")) {
            tagDtoList = hashTagService.getKeywordContent(memberDto);

            if(tag.length() == 0) {
                postDtoList = postService.getPostByMember(memberDto);
            } else {
                postDtoList = postService.getPostByTagAndMember(memberDto, tag);
            }
        } else if(listType.equals("orderList")) {
            orderList = orderService.getAllByMember(memberDto);
        } else if(listType.equals("myBook")) {
            myBookList = myBookService.getAllByBuyerId(memberDto.getId());
        }

        if(productHashTagDtos == null) {
            model.addAttribute("tagList", tagDtoList);
        } else {
            model.addAttribute("tagList", productHashTagDtos);
        }
        model.addAttribute("orderList", orderList);
        model.addAttribute("postList", postDtoList);
        model.addAttribute("productList", productDtos);
        model.addAttribute("member", memberDto);
        model.addAttribute("myBookList", myBookList);

        return "member/profile";
    }

    //작가로 등록
    @PostMapping("/signup/author")
    @PreAuthorize("isAuthenticated()")
    public String signupAuthor(@RequestParam("nickname") String nickname, Principal principal) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());
        try {
            memberService.signupAuthor(memberDto, nickname);
        } catch (DataIntegrityViolationException e) { //작가명이 중복될 경우
            String errorMessage = "redirect:/member?errorMsg=%s".formatted(Ut.url.encode("이미 존재하는 작가명입니다."));
            return errorMessage;
        }
        return "redirect:/member?msg=%s".formatted(Ut.url.encode("작가로 등록되었습니다!"));
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
        return "redirect:/member?msg=%s".formatted(Ut.url.encode("회원정보가 수정되었습니다!"));
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
    public String findPassword(@RequestParam("email") String email, @RequestParam("username") String username) {
        MemberDto member;
        try {
            //회원 찾기
            member = memberService.getMemberByUsername(username);

            if (!member.getEmail().equals(email)) {
                throw new DataNotFoundException("회원을 찾을 수 없습니다.");
            }
        } catch(Exception e) {
            return "redirect:/member/findPassword?errorMsg=%s".formatted(Ut.url.encode("아이디 혹은 이메일을 확인하세요."));
        }
        //임시 비밀번호 생성
        String newPassword = Util.makeRandomPassword();

        //임시 비밀번호로 비밀번호 변겅
        memberService.modifyPassword(member, newPassword);

        //메일 생성 & 발송
        MailTO mail = new MailTO(email, "임시 비밀번호 발급", "회원님의 임시 비밀번호는 " + newPassword + " 입니다.");
        mailService.sendMail(mail);

        return "redirect:/member?msg=%s".formatted(Ut.url.encode("메일로 임시 비밀번호가 발송되었습니다."));
    }
}
