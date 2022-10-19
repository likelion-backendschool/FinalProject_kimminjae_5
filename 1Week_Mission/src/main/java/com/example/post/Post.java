package com.example.post;

import com.example.hashTag.HashTag;
import com.example.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String subject;

    private String content;

    private String contentHtml;

    @ManyToOne
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<HashTag> tagList;

    public PostDto toDto() {
        PostDto postDto = PostDto.builder()
                .id(this.id)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .subject(this.subject)
                .content(this.content)
                .contentHtml(this.contentHtml)
                .member(this.member.toDto())
                .build();
        return postDto;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
