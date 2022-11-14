package com.example.mybook.service;

import com.example.member.enitty.Member;
import com.example.member.dto.MemberDto;
import com.example.mybook.entity.MyBook;
import com.example.mybook.repository.MyBookRepository;
import com.example.product.entity.Product;
import com.example.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyBookService {
    private final MyBookRepository myBookRepository;

    //도서를 내 구매 도서에 등록 -> 도서 보기 가능
    public MyBook create(Member buyer, Product product) {
        MyBook myBook = MyBook.builder()
                .createDate(LocalDateTime.now())
                .buyer(buyer)
                .product(product)
                .build();
        myBookRepository.save(myBook);
        return myBook;
    }

    //buyer가 구매한 모든 도서 목록 반환
    public List<MyBook> getAllByBuyerId(Long id) {
        return myBookRepository.findAllByBuyerId(id);
    }

    public boolean confirmDelete(ProductDto productDto) {
        Product product = productDto.toEntity();
        List<MyBook> myBookList = myBookRepository.findAllByProductId(product.getId());

        if(myBookList.size() > 0) {
            return false;
        }
        return true;
    }

    public void removeMyBook(MemberDto memberDto, List<Product> productList) {
        for(Product product : productList) {
            MyBook myBook = myBookRepository.findByBuyerIdAndProductId(memberDto.getId(), product.getId()).orElse(null);
            myBookRepository.delete(myBook);
        }
    }

    public MyBook getById(long id) {
        return myBookRepository.findById(id).orElse(null);
    }
}
