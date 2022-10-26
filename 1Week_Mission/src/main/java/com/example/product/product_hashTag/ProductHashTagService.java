package com.example.product.product_hashTag;

import com.example.member.MemberDto;
import com.example.post.Post;
import com.example.post.PostDto;
import com.example.post.post_hashTag.HashTag;
import com.example.post.post_hashTag.HashTagDto;
import com.example.post.post_hashTag.HashTagRepository;
import com.example.post.post_keyword.Keyword;
import com.example.post.post_keyword.KeywordService;
import com.example.product.Product;
import com.example.product.ProductDto;
import com.example.product.product_keyword.ProductKeyword;
import com.example.product.product_keyword.ProductKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductHashTagService {
    private final ProductHashTagRepository productHashTagRepository;
    private final ProductKeywordService productKeywordService;

    //해시태그 저장
    public void save(MemberDto member, Product product, String hashTag) {
        //#기준으로 나누고 공백제거
        List<String> keywordList = Arrays.stream(hashTag.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        //키워드 먼저 저장 후 해시태그 저장
        for (String keyword : keywordList) {
            ProductKeyword keyword1 = productKeywordService.getProductKeyword(keyword);
            if (keyword1 == null) {
                keyword1 = productKeywordService.write(keyword);
            }
            ProductHashTag hashTag1 = ProductHashTag.builder()
                    .createDate(LocalDateTime.now())
                    .member(member.toEntity())
                    .product(product)
                    .productKeyword(keyword1)
                    .build();
            productHashTagRepository.save(hashTag1);
        }
    }

    public ProductHashTag create(MemberDto member, Product product, ProductKeyword productKeyword) {
        return ProductHashTag.builder()
                .member(member.toEntity())
                .product(product)
                .productKeyword(productKeyword)
                .createDate(LocalDateTime.now())
                .build();
    }

    //해시태그 수정
    public void modify(MemberDto member, Product product, String hashTag) {
        //#기준으로 나누고 공백제거
        List<String> keywordList = Arrays.stream(hashTag.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        List<ProductHashTag> tagList = productHashTagRepository.findByProduct(product);

        if (tagList.size() > keywordList.size()) {
            for (int i = tagList.size() - 1; i >= keywordList.size(); i--) {
                ProductHashTag hashTag1 = tagList.get(i);
                productHashTagRepository.delete(hashTag1);
            }
        } else if (tagList.size() < keywordList.size()) {
            for (int i = tagList.size(); i < keywordList.size(); i++) {
                tagList.add(create(member, product, null));
            }
        }
        for (int i = 0; i < keywordList.size(); i++) {
            ProductKeyword keyword = productKeywordService.getProductKeyword(keywordList.get(i));
            if (keyword == null) {
                keyword = productKeywordService.write(keywordList.get(i));
            }
            ProductHashTag hashTag1 = tagList.get(i);
            hashTag1.setProductKeyword(keyword);
            productHashTagRepository.save(hashTag1);
        }
    }

    //글 정보로 태그 찾기
    public List<ProductHashTagDto> getTagsByProduct(ProductDto productDto) {
        Product product = productDto.toEntity();
        List<ProductHashTag> tagList = productHashTagRepository.findByProduct(product);
        List<ProductHashTagDto> tagDtoList = tagList.stream().map(ProductHashTag::toDto).toList();

        return tagDtoList;
    }

    //키워드로 태그 찾기
    public List<ProductHashTag> getListByProductKeyword(String tag) {
        ProductKeyword keyword = productKeywordService.getProductKeyword(tag);
        List<ProductHashTag> tagList = productHashTagRepository.findByProductKeyword(keyword);

        return tagList;
    }

    //member로 hasgtag리스트 가져오기
    public List<ProductHashTagDto> getProductHashTagByMember(MemberDto memberDto) {
        List<ProductHashTag> tagList = productHashTagRepository.findByMember(memberDto.toEntity());
        List<ProductHashTagDto> tagDtoList = new ArrayList<>();
        for(ProductHashTag hashTag : tagList) {
            tagDtoList.add(hashTag.toDto());
        }
        return tagDtoList;
    }

    //member로 keyword가져오기 중복x
    public List<String> getKeywordContent(MemberDto memberDto) {
        List<ProductHashTagDto> productHashTagDtos = getProductHashTagByMember(memberDto);
        List<String> keywordList = new ArrayList<>();

        for(ProductHashTagDto productHashTagDto : productHashTagDtos) {
            String keyword = productHashTagDto.getProductKeyword().getContent();

            if(keywordList.contains(keyword)) {
                continue;
            }
            keywordList.add(keyword);
        }
        return keywordList;
    }
}
