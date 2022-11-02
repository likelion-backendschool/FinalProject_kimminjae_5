package com.example.withdraw;

import com.example.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Withdraw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String bankName;

    private String bankAccountNo;

    private long price;

    @ManyToOne
    private Member member;
}
