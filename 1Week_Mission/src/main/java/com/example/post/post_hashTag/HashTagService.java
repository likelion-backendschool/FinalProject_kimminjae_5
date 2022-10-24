package com.example.post.post_hashTag;

import com.example.post.post_keyword.Keyword;
import com.example.post.post_keyword.KeywordService;
import com.example.member.MemberDto;
import com.example.post.Post;
import com.example.post.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        for (String keyword : keywordList) {
            Keyword keyword1 = keywordService.getKeyword(keyword);
            if (keyword1 == null) {
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

    public HashTag create(MemberDto member, Post post, Keyword keyword) {
        return HashTag.builder()
                .member(member.toEntity())
                .post(post)
                .keyword(keyword)
                .createDate(LocalDateTime.now())
                .build();
    }

    //해시태그 수정
    public void modify(MemberDto member, Post post, String hashTag) {
        //#기준으로 나누고 공백제거
        List<String> keywordList = Arrays.stream(hashTag.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        List<HashTag> tagList = hashTagRepository.findByPost(post);

        if (tagList.size() > keywordList.size()) {
            for (int i = tagList.size() - 1; i >= keywordList.size(); i--) {
                HashTag hashTag1 = tagList.get(i);
                hashTagRepository.delete(hashTag1);
            }
        } else if (tagList.size() < keywordList.size()) {
            for (int i = tagList.size(); i < keywordList.size(); i++) {
                tagList.add(create(member, post, null));
            }
        }
        for (int i = 0; i < keywordList.size(); i++) {
            Keyword keyword = keywordService.getKeyword(keywordList.get(i));
            if (keyword == null) {
                keyword = keywordService.write(keywordList.get(i));
            }
            HashTag hashTag1 = tagList.get(i);
            hashTag1.setKeyword(keyword);
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

    public List<HashTagDto> getHashTagByMember(MemberDto memberDto) {
        List<HashTag> tagList = hashTagRepository.findByMember(memberDto.toEntity());
        List<HashTagDto> tagDtoList = new ArrayList<>();
        for(HashTag hashTag : tagList) {
            tagDtoList.add(hashTag.toDto());
        }
        return tagDtoList;
    }
}
