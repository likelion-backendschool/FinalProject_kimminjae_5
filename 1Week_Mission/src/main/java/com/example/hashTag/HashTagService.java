package com.example.hashTag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final HashTagRepository hashTagRepository;
}
