package com.example.product;

import com.example.member.Member;
import com.example.member.MemberDto;
import com.example.member.MemberService;
import com.example.mybook.MyBook;
import com.example.mybook.MyBookService;
import com.example.post.PostDto;
import com.example.post.PostService;
import com.example.post.post_hashTag.HashTag;
import com.example.post.post_hashTag.HashTagDto;
import com.example.product.product_hashTag.ProductHashTag;
import com.example.product.product_hashTag.ProductHashTagDto;
import com.example.product.product_hashTag.ProductHashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductHashTagService productHashTagService;
    private final ProductService productService;
    private final MemberService memberService;

    private final PostService postService;
    private final MyBookService myBookService;

    //도서 목록
    @GetMapping("/list")
    public String productList(Model model, Principal principal, @RequestParam(value = "tag", defaultValue = "") String tag) {
//        List<ProductDto> productDtos = productService.getByMember(memberService.getMemberByUsername(principal.getName()));

        List<ProductDto> productDtos;

        if(tag.length() == 0) {
            productDtos = productService.getAll();
        } else {
            productDtos = productService.getProductByTag(tag);
        }
        model.addAttribute("productList", productDtos);

        return "product/list";
    }

    //도서 등록 폼
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createProduct(Model model, Principal principal) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());

        if(memberDto.getNickname().length() == 0) {
            return "redirect:/product/notAuthor";
        }

        List<PostDto> postDtoList = postService.getPostByMember(memberDto);
        model.addAttribute("postList", postDtoList);
        return "product/create_form";
    }

    @GetMapping("/notAuthor")
    @ResponseBody
    public String notAuthor() {
        return "<script>alert('작가가 되어야 도서를 등록할 수 있습니다! 작가명을 등록하고 작가가 되어보세요!'); location.href='/member';</script>";
    }

    //도서 등록 처리
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createProduct(@RequestParam(value = "subject") String subject,
                                @RequestParam(value = "hashtag") String tags,
                                @RequestParam(value = "price", defaultValue = "0") int price,
                                @RequestParam(value = "postIds", defaultValue = "") String postIds,
                                @RequestParam(value = "description") String description,
                                Principal principal) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());

        List<Long> postIdList = new LinkedList<>();
        List<PostDto> postDtoList = new LinkedList<>();

        if(postIds.length() != 0) {
            for (String ch : postIds.split(",")) {
                postDtoList.add(postService.getPostById(Long.valueOf(ch)));
            }
        }
        productService.create(memberDto, subject, tags, price, description, postDtoList);

        return "redirect:/member";
    }

    //도서 상세
    @GetMapping("/{id}")
    public String productDetail(Model model, @PathVariable("id") long id, Principal principal) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());
        ProductDto productDto = productService.getProductById(id);
        List<ProductHashTagDto> tagList = productHashTagService.getTagsByProduct(productDto);
        List<MyBook> myBooks = myBookService.getAllByBuyerId(memberDto.getId());

        //mybook리스트에 product가 있으면 true -> 구매한 도서
        boolean b = false;

        for(MyBook myBook : myBooks) {
            if(myBook.getProduct().getId() == productDto.getId()) {
                b = true;
            }
        }

        if(memberDto.getId() != productDto.getMemberDto().getId() && !b) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근 권한이 없습니다. 잘못된 접근입니다.");
        }

        model.addAttribute("tagList", tagList);
        model.addAttribute("product", productDto);

        return "product/detail";
    }

    //도서 구매 x 일시 도서 상세
    @GetMapping("/description/{id}")
    public String productDescription(Model model, @PathVariable("id") long id) {
        ProductDto productDto = productService.getProductById(id);
        List<ProductHashTagDto> tagList = productHashTagService.getTagsByProduct(productDto);

        model.addAttribute("tagList", tagList);
        model.addAttribute("product", productDto);

        return "product/description";
    }

    //도서 삭제
    @GetMapping("/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String deleteProduct(@PathVariable("id") long id, Principal principal) {
        ProductDto productDto = productService.getProductById(id);
        //삭제 권한 확인
        if(!productDto.getMemberDto().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        if(myBookService.confirmDelete(productDto)) {
            productService.delete(productDto);
            return "<script>location.href='/member';</script>";
        }
        return "<script>alert('도서를 구매한 유저가 있어 삭제할 수 없습니다!'); location.href='/member';</script>";
    }

    //도서 수정
    @GetMapping("/{id}/modify")
    @PreAuthorize("isAuthenticated()")
    public String modifyProduct(Model model, @PathVariable("id") long id, Principal principal) {
        ProductDto productDto = productService.getProductById(id);
        List<PostDto> postDtoList = postService.getPostByMember(memberService.getMemberByUsername(principal.getName()));
        //수정 권한 확인
        if(!productDto.getMemberDto().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        List<ProductHashTag> tagList = productDto.getProductHashTagList();
        StringBuilder tags = new StringBuilder();
        for(ProductHashTag hashTag : tagList) {
            tags.append("#%s ".formatted(hashTag.getProductKeyword().getContent()));
        }
        model.addAttribute("tags", tags.toString());
        model.addAttribute("postList", postDtoList);
        model.addAttribute("product", productDto);
        return "product/modify";
    }

    //수정 페이지에서 도서에 글 넣기
    @GetMapping("/addPost")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public PostDto addPost(@RequestParam("input_postId") long postId, @RequestParam("input_productId") long productId) {
        ProductDto productDto = productService.getProductById(productId);
        PostDto postDto = postService.getPostById(postId);

        productService.addPostAtProduct(productDto, postDto);

        return postDto;
    }
    //수정 페이지에서 도서에 글 넣기
    @GetMapping("/removePost")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String removePost(@RequestParam("input_postId") long postId, @RequestParam("input_productId") long productId) {
        ProductDto productDto = productService.getProductById(productId);
        PostDto postDto = postService.getPostById(postId);

        productService.removePostAtProduct(productDto, postDto);

        return "success";
    }

    //도서 수정 처리
    @PostMapping("{id}/modify")
    @PreAuthorize("isAuthenticated()")
    public String modifyProduct(Principal principal,
                                @PathVariable("id") long id,
                                @RequestParam("subject") String subject,
                                @RequestParam("hashtag") String hashtags,
                                @RequestParam("price") int price,
                                @RequestParam("description") String description) {
        ProductDto productDto = productService.getProductById(id);

        //수정 권한 확인
        if(!productDto.getMemberDto().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        productService.modify(productDto, subject, hashtags, price, description);

        return "redirect:/product/%d".formatted(id);
    }
}
