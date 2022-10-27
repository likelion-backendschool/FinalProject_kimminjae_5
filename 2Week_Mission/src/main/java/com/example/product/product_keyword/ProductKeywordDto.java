package com.example.product.product_keyword;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductKeywordDto {
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String content;



}
