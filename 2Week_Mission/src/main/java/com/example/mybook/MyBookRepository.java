package com.example.mybook;

import com.example.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyBookRepository extends JpaRepository<MyBook, Long> {


    List<MyBook> findAllByBuyerId(Long id);

    List<MyBook> findAllByProductId(Long id);

    Optional<MyBook> findByBuyerIdAndProductId(Long id, Long id1);
}
