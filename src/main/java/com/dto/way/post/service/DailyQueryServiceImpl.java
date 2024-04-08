package com.dto.way.post.service;

import com.dto.way.post.domain.Daily;
import com.dto.way.post.repository.DailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyQueryServiceImpl implements DailyQueryService {

    private final DailyRepository dailyRepository;

    @Override
    public Daily getDaily(Long postId) {

        Daily daily = dailyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 데일리가 존재하지 않습니다. "));
        return daily;
    }
}
