package com.example.product.product_keyword;

import com.example.post.post_keyword.Keyword;
import com.example.post.post_keyword.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductKeywordService {
    private final ProductKeywordRepository productKeywordRepository;

    public ProductKeyword write(String keyword) {
        ProductKeyword keyword1 = ProductKeyword.builder()
                .createDate(LocalDateTime.now())
                .content(keyword)
                .build();
        productKeywordRepository.save(keyword1);
        return keyword1;
    }

    public ProductKeyword getProductKeyword(String keyword) {
        Optional<ProductKeyword> optionalKeyword = productKeywordRepository.findByContent(keyword);
        if(optionalKeyword.isPresent()) {
            return optionalKeyword.get();
        } else {
            return null;
        }
    }
}
