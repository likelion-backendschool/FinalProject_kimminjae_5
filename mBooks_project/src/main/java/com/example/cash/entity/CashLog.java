package com.example.cash.entity;

import com.example.member.enitty.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class CashLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @ManyToOne(fetch = LAZY)
    private Member member;

    private long price; //변동

    private String eventType;

    public CashLog(long id) {
        this.id = id;
    }
}
