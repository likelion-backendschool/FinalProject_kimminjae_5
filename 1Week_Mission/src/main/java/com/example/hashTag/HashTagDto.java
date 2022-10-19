package com.example.hashTag;

import com.example.keyword.Keyword;
import com.example.member.Member;
import com.example.post.Post;
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
