package com.example.product;

import com.example.Util;
import com.example.member.Member;
import com.example.post.Post;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    private int price;

    @ManyToOne
    private Member member;

    @ManyToMany
    @JsonBackReference
    private List<Post> postList;

    public ProductDto toDto() {
        return ProductDto.builder()
                .id(this.id)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .subject(this.subject)
                .price(this.price)
                .memberDto(this.member.toDto())
                .postDtoList(Util.toPostDtoList(this.postList))
                .build();
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
