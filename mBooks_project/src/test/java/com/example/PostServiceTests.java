package com.example;

import com.example.member.Member;
import com.example.member.MemberRepository;
import com.example.post.PostDto;
import com.example.post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostServiceTests {
    void testData() {
        Member member = Member.builder()
                .username("username").authLevel(3).createDate(LocalDateTime.now()).email("min@123").password("password")
                .build();
        memberRepository.save(member);
        PostDto post1 = postService.write(member.toDto(), "subject1", "#a #b", "content1");
        PostDto post2 = postService.write(member.toDto(), "subject2", "#a #b", "content2");
    }
    @Autowired
    private PostService postService;
    @Autowired
    private MemberRepository memberRepository;
    @Test
    void write_post() {
        Member member = Member.builder()
                .username("username").authLevel(3).createDate(LocalDateTime.now()).email("min@123").password("password")
                .build();
        memberRepository.save(member);
        PostDto post1 = postService.write(member.toDto(), "subject1", "#a #b", "content1");

        assertThat(post1.getSubject()).isEqualTo("subject1");
    }
    @Test
    void select_post_All() {
        testData();
        List<PostDto> postDtoList = postService.getAllPost();

        assertThat(postDtoList.size()).isEqualTo(2);
    }
    @Test
    void select_post() {
        testData();
        PostDto post = postService.getPostById(2L);

        assertThat(post.getSubject()).isEqualTo("subject2");
    }
    @Test
    void modify_post() {
        testData();
        PostDto post = postService.getPostById(1L);
        assertThat(post.getSubject()).isEqualTo("subject1");

        postService.modify(post, "modifySubject", "#a #b", "modifyContent");
        post = postService.getPostById(1L);
        assertThat(post.getSubject()).isEqualTo("modifySubject");
    }
    @Test
    void delete_post() {
        testData();
        PostDto post = postService.getPostById(1L);
        postService.delete(post);

        try {
            post = postService.getPostById(1L);
        } catch (Exception e) { System.out.println("success"); }

    }
}
