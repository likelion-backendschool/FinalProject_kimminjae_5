package com.example;

import com.example.post.post_keyword.Keyword;
import com.example.post.post_keyword.KeywordDto;
import com.example.post.Post;
import com.example.post.PostDto;
import com.example.product.Product;
import com.example.product.ProductDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Util {

    //랜덤 문자열 생성
    public static String makeRandomPassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
    //Post Dto화
    public static List<PostDto> toPostDtoList(List<Post> postList) {
        List<PostDto> postDtoList = postList.stream().map(Post :: toDto).toList();
        System.out.println(postDtoList);
        return postDtoList;
    }
    public static List<ProductDto> toDtoList(List<Product> productList) {
        List<ProductDto> postDtoList = productList.stream().map(Product :: toDto).toList();
        System.out.println(postDtoList);
        return postDtoList;
    }

    //keyword Dto화
    public static Set<KeywordDto> toDtoList(Set<Keyword> keywords) {
        Set<KeywordDto> keywordDtos = keywords.stream().map(Keyword :: toDto).collect(Collectors.toSet());
        System.out.println(keywordDtos);
        return keywordDtos;
    }

    public static List<Post> toEntityList(List<PostDto> postDtoList) {
//        List<Post> postList = postDtoList.stream().map(PostDto :: toEntity).toList();
        List<Post> postList = new LinkedList<>();
        for(PostDto postDto : postDtoList) {
            postList.add(postDto.toEntity());
        }

        return postList;
    }
//    public static LocalDateTime parse(String dateText) {
//        return parse("yyyy-MM-dd HH:mm:ss.SSSSSS", dateText);
//    }
}
