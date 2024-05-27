package com.dto.way.post.service.historyService;

import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface HistoryQueryService {
    HistoryResponseDto.GetHistoryResultDto getHistoryResult(HttpServletRequest httpServletRequest, Long postId);

    HistoryResponseDto.GetHistoryListResultDto getHistoryListByRange(HttpServletRequest httpServletRequest, Double longitude1, Double latitude1, Double longitude2, Double latitude2);
}
