package com.example.product;

import com.example.AppConfig;
import com.example.Util;
import com.example.member.Member;
import com.example.post.Post;
import com.example.post.post_hashTag.HashTag;
import com.example.product.product_hashTag.ProductHashTag;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String subject;

    @JsonIgnore
    private int price;

    @ManyToOne
    private Member member;

    @ManyToMany
    @JsonProperty("bookChapters")
    private List<Post> postList;

    @Column(columnDefinition = "TEXT")
    @JsonIgnore
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<ProductHashTag> productHashTagList;

    public ProductDto toDto() {
        return ProductDto.builder()
                .id(this.id)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .subject(this.subject)
                .price(this.price)
                .memberDto(this.member.toDto())
                .postDtoList(Util.toPostDtoList(this.postList))
                .productHashTagList(this.productHashTagList)
                .description(this.description)
                .build();
    }
//    public Product(long id) {
//        super(id);
//    }

    public int getSalePrice() {
        return getPrice();
    }

    public int getWholesalePrice() {
        return (int) Math.ceil(getPrice() * AppConfig.getWholesalePriceRate());
    }

    public boolean isOrderable() {
        return true;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
