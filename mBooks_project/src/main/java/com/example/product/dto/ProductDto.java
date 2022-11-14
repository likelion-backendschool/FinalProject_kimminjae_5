package com.example.product.dto;

import com.example.product.entity.Product;
import com.example.util.Util;
import com.example.member.dto.MemberDto;
import com.example.post.dto.PostDto;
import com.example.product.product_hashTag.ProductHashTag;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String subject;
    private int price;
    private MemberDto memberDto;
    private List<PostDto> postDtoList;
    private String description;

    private List<ProductHashTag> productHashTagList;
    public Product toEntity() {
        return Product.builder()
                .id(this.id)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .subject(this.subject)
                .price(this.price)
                .member(this.memberDto.toEntity())
                .postList(Util.toEntityList(this.postDtoList))
                .productHashTagList(this.productHashTagList)
                .description(this.description)
                .build();
    }
}
