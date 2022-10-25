package com.example.cart;

import com.example.member.Member;
import com.example.member.MemberService;
import com.example.product.Product;
import com.example.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final MemberService memberService;
    private final CartService cartService;
    private final ProductService productService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String cartList(Principal principal, Model model) {
        Member buyer = memberService.getMemberByUsername(principal.getName()).toEntity();

        List<CartItem> items = cartService.getItemsByBuyer(buyer);

        model.addAttribute("cartItems", items);

        return "cart/list";
    }
    @GetMapping("/add/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String addItem(Principal principal, @PathVariable long id) {
        System.out.println("add");

        Member buyer = memberService.getMemberByUsername(principal.getName()).toEntity();
        Product product = productService.getProductById(id).toEntity();

        cartService.addItem(buyer, product);

        return "success";
    }

    @GetMapping("/remove/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String removeItem(Principal principal, @PathVariable long id) {
        System.out.println("add");

        Member buyer = memberService.getMemberByUsername(principal.getName()).toEntity();
        Product product = productService.getProductById(id).toEntity();

        cartService.removeItem(buyer, product);

        return "success";
    }

}
