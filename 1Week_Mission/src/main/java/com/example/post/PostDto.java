package com.example.post;

import com.example.post.post_hashTag.HashTag;
import com.example.member.MemberDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String subject;

    private String content;

    private String contentHtml;

    private MemberDto member;

    private List<HashTag> hashTagList;

    public Post toEntity() {
        return Post.builder()
                .id(this.id)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .subject(this.subject)
                .content(this.content)
                .contentHtml(this.contentHtml)
                .member(this.member.toEntity())
                .hashTagList(this.hashTagList)
                .build();
    }
}
