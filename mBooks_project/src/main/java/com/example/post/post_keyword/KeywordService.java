package com.example.post.post_keyword;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public Keyword write(String keyword) {
        Keyword keyword1 = Keyword.builder()
                .createDate(LocalDateTime.now())
                .content(keyword)
                .build();
        keywordRepository.save(keyword1);
        return keyword1;
    }

    public Keyword getKeyword(String keyword) {
        Optional<Keyword> optionalKeyword = keywordRepository.findByContent(keyword);
        if(optionalKeyword.isPresent()) {
            return optionalKeyword.get();
        } else {
            return null;
        }
    }
}
