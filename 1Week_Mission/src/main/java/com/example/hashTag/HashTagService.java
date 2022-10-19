package com.example.hashTag;

import com.example.keyword.Keyword;
import com.example.keyword.KeywordService;
import com.example.member.MemberDto;
import com.example.post.Post;
import com.example.post.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final HashTagRepository hashTagRepository;
    private final KeywordService keywordService;

    //해시태그 저장
    public void save(MemberDto member, Post post, String hashTag) {
        //#기준으로 나누고 공백제거
        List<String> keywordList = Arrays.stream(hashTag.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        //키워드 먼저 저장 후 해시태그 저장
        for(String keyword : keywordList) {
            Keyword keyword1 = keywordService.getKeyword(keyword);
            if(keyword1 == null) {
                keyword1 = keywordService.write(keyword);
            }
            HashTag hashTag1 = HashTag.builder()
                    .createDate(LocalDateTime.now())
                    .member(member.toEntity())
                    .post(post)
                    .keyword(keyword1)
                    .build();
            hashTagRepository.save(hashTag1);
        }
    }

    //글 정보로 태그 찾기
    public List<HashTagDto> getTagsByPost(PostDto postDto) {
        Post post = postDto.toEntity();
        List<HashTag> tagList = hashTagRepository.findByPost(post);
        List<HashTagDto> tagDtoList = tagList.stream().map(HashTag::toDto).toList();

        return tagDtoList;
    }

    //키워드로 태그 찾기
    public List<HashTag> getListByKeyword(String tag) {
        Keyword keyword = keywordService.getKeyword(tag);
        List<HashTag> tagList = hashTagRepository.findByKeyword(keyword);

        return tagList;
    }
}
