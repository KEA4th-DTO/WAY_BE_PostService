package com.dto.way.post.service;

import com.dto.way.post.domain.Daily;

public interface DailyQueryService {
    Daily getDaily(Long postId);
}
