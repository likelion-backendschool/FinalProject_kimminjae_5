package com.example;

import com.example.post.PostDto;
import com.example.post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostServiceTests {
    @Autowired
    private PostService postService;
    @Test
    void write_post() {
        PostDto post1 = postService.write("subject1", "content1", "contentHtml1");
        PostDto post2 = postService.write("subject2", "content2", "contentHtml2");

        assertThat(post1.getSubject()).isEqualTo("subject1");
    }
    @Test
    void select_post_All() {
        List<PostDto> postDtoList = postService.getAllPost();

        assertThat(postDtoList.size()).isEqualTo(2);
    }
    @Test
    void select_post() {
        PostDto post = postService.getPostById(2L);

        assertThat(post.getSubject()).isEqualTo("subject2");
    }
    @Test
    void modify_post() {
        PostDto post = postService.getPostById(1L);
        assertThat(post.getSubject()).isEqualTo("subject1");

        postService.modify("modifySubject", "modifyContent");
        post = postService.getPostById(1L);
        assertThat(post.getSubject()).isEqualTo("modifySubject");
    }
    @Test
    void delete_post() {
        PostDto post = postService.getPostById(1L);
        postService.delete(post);

        post = postService.getPostById(1L);

        assertThat(post).isEqualTo(null);
    }
}
