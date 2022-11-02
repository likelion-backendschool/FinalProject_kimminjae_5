package com.example.withdraw;

import com.example.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WithdrawService {
    private final WithdrawRepository withdrawRepository;

    @Transactional
    public Withdraw create(String bankName, String bankAccountNo, long price, MemberDto memberDto) {
        Withdraw withdraw = Withdraw.builder()
                .createDate(LocalDateTime.now())
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .price(price)
                .member(memberDto.toEntity())
                .build();
        withdrawRepository.save(withdraw);

        return withdraw;
    }

    public List<Withdraw> getAll() {
        return withdrawRepository.findAll();
    }

    @Transactional
    public void withdrawDone(long id) {
        Withdraw withdraw = withdrawRepository.findById(id).orElse(null);
        withdraw.setWithdraw(true);

        withdrawRepository.save(withdraw);


    }

    public Withdraw getById(long id) {
        return withdrawRepository.findById(id).orElse(null);
    }

    public List<Withdraw> getByMember(MemberDto memberDto) {
        return withdrawRepository.findAllByMemberId(memberDto.getId());
    }

    @Transactional
    public void cancel(Withdraw withdraw) {
        withdraw.setCanceled(true);
        withdrawRepository.save(withdraw);
    }
}
