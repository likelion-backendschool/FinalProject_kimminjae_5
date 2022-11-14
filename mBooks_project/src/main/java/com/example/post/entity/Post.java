package com.example.post.entity;

import com.example.post.dto.PostDto;
import com.example.post.post_hashTag.HashTag;
import com.example.member.enitty.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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
@JsonIncludeProperties({"id", "subject", "content", "contentHtml"})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String contentHtml;

    @ManyToOne
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<HashTag> hashTagList;

    public PostDto toDto() {
        PostDto postDto = PostDto.builder()
                .id(this.id)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .subject(this.subject)
                .content(this.content)
                .contentHtml(this.contentHtml)
                .member(this.member.toDto())
                .hashTagList(this.hashTagList)
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
