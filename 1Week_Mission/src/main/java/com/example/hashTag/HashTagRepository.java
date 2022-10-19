package com.example.hashTag;

import com.example.keyword.Keyword;
import com.example.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    List<HashTag> findByPost(Post post);

    List<HashTag> findByKeyword(Keyword keyword);

    HashTag findByKeyword(String keyword);
}
