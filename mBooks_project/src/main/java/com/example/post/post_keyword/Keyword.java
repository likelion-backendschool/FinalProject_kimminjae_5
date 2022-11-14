package com.example.post.post_keyword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
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