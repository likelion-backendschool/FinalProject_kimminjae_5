package com.example.withdraw;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {
    List<Withdraw> findAllByMemberId(Long id);
}
