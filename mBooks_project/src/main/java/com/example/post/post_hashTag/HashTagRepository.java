package com.example.post.post_hashTag;

import com.example.post.post_keyword.Keyword;
import com.example.member.enitty.Member;
import com.example.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    List<HashTag> findByPost(Post post);

    List<HashTag> findByKeyword(Keyword keyword);

    HashTag findByKeyword(String keyword);

    List<HashTag> findByMember(Member toEntity);
}
