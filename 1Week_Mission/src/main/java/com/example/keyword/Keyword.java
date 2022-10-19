package com.example.keyword;

import com.example.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String content;

    public KeywordDto toDto() {
        return KeywordDto.builder()
                .id(id)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .content(this.content)
                .build();
    }

}