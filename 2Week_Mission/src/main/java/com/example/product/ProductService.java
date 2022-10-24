package com.example.product;

import com.example.CommonUtil;
import com.example.DataNotFoundException;
import com.example.Util;
import com.example.member.Member;
import com.example.member.MemberDto;
import com.example.member.MemberService;
import com.example.post.Post;
import com.example.post.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberService memberService;


    public void create(MemberDto memberDto, String subject, String tags, int price, List<PostDto> postDtoList) {
        Product product = Product.builder()
                .createDate(LocalDateTime.now())
                .subject(subject)
                .price(price)
                .member(memberDto.toEntity())
                .postList(Util.toEntityList(postDtoList))
                .build();

        productRepository.save(product);

//        productHashTagService.save(member, product, hashTag);

    }

    public List<ProductDto> getByMember(MemberDto memberDto) {
        Member member = memberDto.toEntity();

        List<ProductDto> productDtos = Util.toDtoList(productRepository.findByMember(member));

        return productDtos;
    }

    public ProductDto getProductById(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if(optionalProduct.isPresent()) {
            return optionalProduct.get().toDto();
        } else {
            throw new DataNotFoundException("도서가 존재하지 않습니다.");
        }
    }

    public void delete(ProductDto productDto) {
        Product product = productDto.toEntity();

        productRepository.delete(product);
    }

    public List<ProductDto> getAll() {
        return Util.toDtoList(productRepository.findAll());
    }
}
