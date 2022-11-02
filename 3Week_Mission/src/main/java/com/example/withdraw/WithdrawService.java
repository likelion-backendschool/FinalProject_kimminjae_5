package com.example.withdraw;

import com.example.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<Withdraw> getAll() {
        return withdrawRepository.findAll();
    }

    public void withdrawDone(long id) {
        Withdraw withdraw = withdrawRepository.findById(id).orElse(null);
        withdraw.setWithdraw(true);

        withdrawRepository.save(withdraw);


    }

    public Withdraw getById(long id) {
        return withdrawRepository.findById(id).orElse(null);
    }
}
