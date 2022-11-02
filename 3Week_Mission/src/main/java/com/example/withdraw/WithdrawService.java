package com.example.withdraw;

import com.example.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WithdrawService {
    private final WithdrawRepository withdrawRepository;

    public void create(String bankName, String bankAccountNo, long price, MemberDto memberDto) {
        Withdraw withdraw = Withdraw.builder()
                .createDate(LocalDateTime.now())
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .price(price)
                .member(memberDto.toEntity())
                .build();
        withdrawRepository.save(withdraw);
    }
}
