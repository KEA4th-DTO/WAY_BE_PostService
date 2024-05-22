package com.dto.way.post.service.historyService;

import com.dto.way.post.domain.History;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import org.springframework.security.core.Authentication;

public interface HistoryQueryService {
    History getHistory(Long postId);

    HistoryResponseDto.GetHistoryListResultDto getHistoryListByRange(Authentication auth, Double longitude1, Double latitude1, Double longitude2, Double latitude2);
}
