package com.example.post.post_hashTag;

import com.example.post.post_keyword.Keyword;
import com.example.member.enitty.Member;
import com.example.post.entity.Post;
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

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
