package com.example.withdraw;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawService {
    private final WithdrawRepository withdrawRepository;
}
