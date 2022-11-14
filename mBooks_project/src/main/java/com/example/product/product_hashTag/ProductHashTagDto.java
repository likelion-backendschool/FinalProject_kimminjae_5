package com.example.product.product_hashTag;

import com.example.member.enitty.Member;
import com.example.product.entity.Product;
import com.example.product.product_keyword.ProductKeyword;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductHashTagDto {
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private Member member;

    private Product product;

    private ProductKeyword productKeyword;
}
