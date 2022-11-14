package com.example.mybook.entity;

import com.example.member.enitty.Member;
import com.example.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @ManyToOne
    @JsonIgnore
    private Member buyer;

    @ManyToOne
    private Product product;
}
