package com.example.hashTag;

import com.example.keyword.Keyword;
import com.example.member.Member;
import com.example.post.Post;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Keyword keyword;

    public HashTagDto toDto() {
        return HashTagDto.builder()
                .id(id)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .member(this.member)
                .post(this.post)
                .keyword(this.keyword)
                .build();

    }
}
