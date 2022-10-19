package com.example.post;

import com.example.CommonUtil;
import com.example.DataNotFoundException;
import com.example.hashTag.HashTag;
import com.example.hashTag.HashTagService;
import com.example.member.Member;
import com.example.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final HashTagService hashTagService;

    //글 작성
    public PostDto write(MemberDto member, String subject, String hashTag, String content) {
        Post post = Post.builder()
                .subject(subject)
                .content(content)
                .contentHtml(CommonUtil.markdown(content))
                .createDate(LocalDateTime.now())
                .member(member.toEntity())
                .build();
        postRepository.save(post);

        hashTagService.save(member, post, hashTag);

        return post.toDto();
    }

    //전체 글 불러오기
    public List<PostDto> getAllPost() {
//        List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("createDate"));
//
        List<Post> postList = postRepository.findAll();
        List<PostDto> postDtoList = new ArrayList<>();

        for(Post post : postList) {
            postDtoList.add(post.toDto());
        }
//        postDtoList.sort();
        return postDtoList;
    }

    //id로 글 찾기
    public PostDto getPostById(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isPresent()) {
            return optionalPost.get().toDto();
        } else {
            throw new DataNotFoundException("글을 찾을 수 없습니다.");
        }
    }

    //글 수정
    public void modify(PostDto postDto, String subject, String hashTag, String content) {
        Optional<Post> optionalPost = postRepository.findById(postDto.getId());

        Post post;
        if(optionalPost.isPresent()) {
            post = optionalPost.get();
        } else {
            throw new DataNotFoundException("글이 존재하지 않습니다.");
        }
        post.setSubject(subject);
        post.setContent(content);
        post.setContentHtml(CommonUtil.markdown(content));
        post.setUpdateDate(LocalDateTime.now());

        postRepository.save(post);

        hashTagService.modify(postDto.getMember(), post, hashTag);

        postRepository.save(post);
    }

    //글 삭제
    public void delete(PostDto postDto) {
        Optional<Post> optionalPost = postRepository.findById(postDto.getId());
        if(optionalPost.isPresent()) {
            postRepository.delete(optionalPost.get());
        } else {
            throw new DataNotFoundException("글이 존재하지 않습니다.");
        }
    }

    //태그로 글 불러오기
    public List<PostDto> getPostByTag(String tag) {
        //태그 키워드로 해시태그를 찾고
        List<HashTag> tagList = hashTagService.getListByKeyword(tag);

        //해시태그로 글을 찾아 리스트로 반환한다
        List<PostDto> postDtoList = new ArrayList<>();

        for(HashTag hashTag : tagList) {
            postDtoList.add(hashTag.getPost().toDto());
        }
        return postDtoList;
    }

    public void removeHashTag(Post post, HashTag hashTag1) {
        post.getHashTagList().remove(hashTag1);
        postRepository.save(post);
    }
}
