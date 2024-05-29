package com.dto.way.post.service.historyService;

import com.dto.way.post.domain.History;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public interface HistoryQueryService {
    HistoryResponseDto.GetHistoryResultDto getHistoryResult(HttpServletRequest httpServletRequest, Long postId);

    HistoryResponseDto.GetHistoryListResultDto getHistoryListByRange(HttpServletRequest httpServletRequest, Double longitude1, Double latitude1, Double longitude2, Double latitude2);

    Page<History> findHistoryByTitle(Integer page, String title);

    Page<History> findHistoryByBody(Integer page, String body);

}
