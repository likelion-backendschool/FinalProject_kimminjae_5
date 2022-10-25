package com.example.cart;

import com.example.DataNotFoundException;
import com.example.member.Member;
import com.example.product.Product;
import com.example.product.ProductDto;
import com.example.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public String addItem(Member buyer, Product product) {
        CartItem oldCartItem = cartItemRepository.findByBuyerIdAndProductId(buyer.getId(), product.getId()).orElse(null);

        List<ProductDto> productDtos = productService.getByMember(buyer.toDto());
        
        for(ProductDto productDto : productDtos) {
            if(productDto.getId() == product.getId()) {
                return "my product";
            }
        }

        if (oldCartItem != null) {
//            throw new DataNotFoundException("이미 장바구니에 있습니다.");
//            return oldCartItem;
            return "already exist";
        }
        CartItem cartItem = CartItem.builder()
                .createDate(LocalDateTime.now())
                .buyer(buyer)
                .product(product)
                .build();

        cartItemRepository.save(cartItem);

//        return cartItem;
        return "addItem";
    }

    @Transactional
    public boolean removeItem(Member buyer, Product product) {
        CartItem oldCartItem = cartItemRepository.findByBuyerIdAndProductId(buyer.getId(), product.getId()).orElse(null);

        if(oldCartItem != null) {
            cartItemRepository.delete(oldCartItem);
            return true;
        }
        return false;
    }
    public boolean hasItem(Member buyer, Product product) {
        return cartItemRepository.existsByBuyerIdAndProductId(buyer.getId(), product.getId());
    }
    public List<CartItem> getItemsByBuyer(Member buyer) {
        return cartItemRepository.findAllByBuyerId(buyer.getId());
    }
    public void removeItem(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }
    public void removeItem(Member buyer, long productId) {
        Product product = Product.builder().id(productId).build();
        removeItem(buyer, product);
    }
}
