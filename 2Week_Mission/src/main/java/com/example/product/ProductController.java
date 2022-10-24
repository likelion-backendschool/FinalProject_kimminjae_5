package com.example.product;

import com.example.member.Member;
import com.example.member.MemberDto;
import com.example.member.MemberService;
import com.example.post.PostDto;
import com.example.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final MemberService memberService;

    private final PostService postService;

    //도서 목록
    @GetMapping("/list")
    public String productList(Model model, Principal principal) {
//        List<ProductDto> productDtos = productService.getByMember(memberService.getMemberByUsername(principal.getName()));
        List<ProductDto> productDtos = productService.getAll();

        model.addAttribute("productList", productDtos);

        return "product/list";
    }

    //도서 등록 폼
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createProduct(Model model, Principal principal) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());

        if(memberDto.getNickname() == null) {
            return "redirect:/member";
        }

        List<PostDto> postDtoList = postService.getPostByMember(memberDto);
        model.addAttribute("postList", postDtoList);
        return "product/create_form";
    }

    //도서 등록 처리
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createProduct(@RequestParam(value = "subject") String subject,
                                @RequestParam(value = "hashtag") String tags,
                                @RequestParam(value = "price", defaultValue = "0") int price,
                                @RequestParam(value = "postIds", defaultValue = "") String postIds,
//                                @RequestParam(value = "input_postList") List<Long> postIdList,
                                Principal principal) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());

        List<Long> postIdList = new ArrayList<>();
        List<PostDto> postDtoList = new ArrayList<>();

        if(postIds.length() != 0) {
            for (String ch : postIds.split(",")) {
                postIdList.add(Long.valueOf(ch));
            }
            for (long id : postIdList) {
                postDtoList.add(postService.getPostById(id));
            }
        }
        productService.create(memberDto, subject, tags, price, postDtoList);

        return "redirect:/member";
    }

    //도서 상세
    @GetMapping("/{id}")
    public String productDetail(Model model, @PathVariable("id") long id) {
        ProductDto productDto = productService.getProductById(id);

        model.addAttribute("product", productDto);

        return "product/detail";
    }

    //도서 삭제
    @GetMapping("/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteProduct(@PathVariable("id") long id, Principal principal) {
        ProductDto productDto = productService.getProductById(id);
        //삭제 권한 확인
        if(!productDto.getMemberDto().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        productService.delete(productDto);
        return "redirect:/";
    }
}
