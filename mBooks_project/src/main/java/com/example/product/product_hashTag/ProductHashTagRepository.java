package com.example.product.product_hashTag;

import com.example.member.enitty.Member;
import com.example.product.entity.Product;
import com.example.product.product_keyword.ProductKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductHashTagRepository extends JpaRepository<ProductHashTag, Long> {
    List<ProductHashTag> findByProduct(Product product);

    List<ProductHashTag> findByProductKeyword(ProductKeyword productKeyword);

    ProductHashTag findByProductKeyword(String Productkeyword);

    List<ProductHashTag> findByMember(Member member);
}
