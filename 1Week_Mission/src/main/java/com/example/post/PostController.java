package com.example.post;

import com.example.member.MemberDto;
import com.example.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final MemberService memberService;
    private final PostService postService;
    // 글 목록
    @GetMapping("/list")
    public String list(Model model) {

        List<PostDto> postDtoList = postService.getAllPost();
        model.addAttribute("postList", postDtoList);
        return "post/list";
    }

    // 글 상세페이지
    @GetMapping("/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        PostDto postDto = postService.getPostById(id);
        model.addAttribute("post", postDto);

        return "post/detail";
    }

    // 글 작성
    @GetMapping("/write")
    public String write() {
        return "post/write";
    }
    @PostMapping("/write")
    public String write(@RequestParam("subject") String subject, @RequestParam("content") String content, Principal principal) {
        MemberDto member = memberService.getMemberByUsername(principal.getName());
        postService.write(member, subject, content);
        return "redirect:/";
    }

    // 글 수정
    @GetMapping("/{id}/modify")
    public String modify(Model model, @PathVariable("id") Long id) {
        PostDto postDto = postService.getPostById(id);
        model.addAttribute("post", postDto);
        return "post/modify";
    }

    //글 수정 처리
    @PostMapping("/{id}/modify")
    public String modify(@PathVariable("id") long id) {
        return "redirect:/";
    }

    //글 삭제
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        PostDto postDto = postService.getPostById(id);
        //삭제 권한 확인
        if(!postDto.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        postService.delete(postDto);
        return "redirect:/";
    }
}
