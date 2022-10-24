package com.example.product;

import com.example.Util;
import com.example.member.MemberDto;
import com.example.post.PostDto;
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

    public Product toEntity() {
        return Product.builder()
                .id(this.id)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .subject(this.subject)
                .price(this.price)
                .member(this.memberDto.toEntity())
                .postList(Util.toEntityList(this.postDtoList))
                .build();
    }
}
