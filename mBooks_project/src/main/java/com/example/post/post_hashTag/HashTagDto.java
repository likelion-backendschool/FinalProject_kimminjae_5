package com.example.post.post_hashTag;

import com.example.post.post_keyword.Keyword;
import com.example.member.enitty.Member;
import com.example.post.entity.Post;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HashTagDto {
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private Member member;

    private Post post;

    private Keyword keyword;
}
