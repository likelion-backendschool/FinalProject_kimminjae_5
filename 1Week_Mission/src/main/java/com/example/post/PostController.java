package com.example.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    // /post/list
    @GetMapping("/list")
    public String list(Model model) {
        List<PostDto> postDtoList = postService.getAllPost();
        model.addAttribute("postList", postDtoList);
        return "post/list";
    }
    // /post/{id}
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id) {
        return "post/detail";
    }
    // /post/write
    @GetMapping("/write")
    public String write() {
        return "post/write";
    }
//    @PostMapping("/write")
//    public String write() {
//        return "redirect:/";
//    }
    // /post/{id}/modify
    @GetMapping("/{id}/modify")
    public String modify() {
        return "post/modify";
    }
    // /post/{id}/modify - post
//    @PostMapping("/{id}/modify")
//    public String modify() {
//        return "redirect:/";
//    }
    // /post/{id}/delete
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        return "redirect:/";
    }
}
