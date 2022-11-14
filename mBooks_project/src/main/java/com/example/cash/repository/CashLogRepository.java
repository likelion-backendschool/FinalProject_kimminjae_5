package com.example.cash.repository;

import com.example.cash.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}
