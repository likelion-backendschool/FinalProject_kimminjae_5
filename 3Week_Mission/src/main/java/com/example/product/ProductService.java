package com.example.product;

import com.example.CommonUtil;
import com.example.DataNotFoundException;
import com.example.Util;
import com.example.member.Member;
import com.example.member.MemberDto;
import com.example.member.MemberService;
import com.example.post.Post;
import com.example.post.PostDto;
import com.example.post.post_hashTag.HashTag;
import com.example.product.product_hashTag.ProductHashTag;
import com.example.product.product_hashTag.ProductHashTagDto;
import com.example.product.product_hashTag.ProductHashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductHashTagService productHashTagService;


    public void create(MemberDto memberDto, String subject, String tags, int price, String description, List<PostDto> postDtoList) {
        Product product = Product.builder()
                .createDate(LocalDateTime.now())
                .subject(subject)
                .price(price)
                .member(memberDto.toEntity())
                .postList(Util.toEntityList(postDtoList))
                .description(description)
                .build();

        productRepository.save(product);

        productHashTagService.save(memberDto, product,tags);
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

    public void addPostAtProduct(ProductDto productDto, PostDto postDto) {
        Product product = productDto.toEntity();
        Post post = postDto.toEntity();

        List<Post> postList = product.getPostList();
        for(Post post1 : postList) {
            if(post1.getId() == post.getId()) throw new DataNotFoundException("도서에 글이 존재합니다.");
        }
        postList.add(post);

        productRepository.save(product);
    }

    public void removePostAtProduct(ProductDto productDto, PostDto postDto) {
        Product product = productDto.toEntity();
        Post post = postDto.toEntity();
        List<Post> postList = product.getPostList();

        for(Post post1 : postList) {
            if(post1.getId() == post.getId()) {
                postList.remove(post1);
                break;
            }
        }
        productRepository.save(product);
    }

    public void modify(ProductDto productDto, String subject, String hashtags, int price, String description) {
        Product product = productDto.toEntity();
        product.setUpdateDate(LocalDateTime.now());
        product.setSubject(subject);
        product.setPrice(price);
        product.setDescription(description);

        //해시태그 수정 로직 추가

        productRepository.save(product);

        productHashTagService.modify(productDto.getMemberDto(), product, hashtags);
    }

    public List<ProductDto> getProductByTag(String tag) {
        //태그 키워드로 해시태그를 찾고
        List<ProductHashTag> tagList = productHashTagService.getListByProductKeyword(tag);

        //해시태그로 글을 찾아 리스트로 반환한다
        List<ProductDto> productDtoList = new ArrayList<>();

        for(ProductHashTag hashTag : tagList) {
            productDtoList.add(hashTag.getProduct().toDto());
        }
        return productDtoList;
    }

    public List<ProductDto> getProductByTagAndMember(MemberDto memberDto, String tag) {
        Member member = memberDto.toEntity();
        //태그 키워드로 해시태그를 찾고
        List<ProductHashTag> tagList = productHashTagService.getListByProductKeyword(tag);

        //해시태그로 글을 찾아 리스트로 반환한다
        List<ProductDto> productDtoList = new ArrayList<>();

        for(ProductHashTag hashTag : tagList) {
            if(hashTag.getMember().getId() == member.getId()) {
                productDtoList.add(hashTag.getProduct().toDto());
            }
        }
        return productDtoList;
    }
}
