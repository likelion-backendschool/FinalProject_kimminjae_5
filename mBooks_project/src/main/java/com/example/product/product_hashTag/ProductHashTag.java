package com.example.product.product_hashTag;

import com.example.member.enitty.Member;
import com.example.product.entity.Product;
import com.example.product.product_keyword.ProductKeyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductHashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Product product;

    @ManyToOne
    private ProductKeyword productKeyword;

    public ProductHashTagDto toDto() {
        return ProductHashTagDto.builder()
                .id(id)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .member(this.member)
                .product(this.product)
                .productKeyword(this.productKeyword)
                .build();

    }

    public void setProductKeyword(ProductKeyword productKeyword) {
        this.productKeyword = productKeyword;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
