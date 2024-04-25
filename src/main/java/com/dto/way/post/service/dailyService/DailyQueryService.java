package com.dto.way.post.service.dailyService;

import com.dto.way.post.domain.Daily;
import com.dto.way.post.domain.Post;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;

import java.util.List;

public interface DailyQueryService {
    Daily getDaily(Long postId);

    DailyResponseDto.GetDailyListResultDto getDailyListByRange(Double longitude1, Double latitude1, Double longitude2, Double latitude2);

}
