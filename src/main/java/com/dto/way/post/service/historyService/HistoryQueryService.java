package com.dto.way.post.service.historyService;

import com.dto.way.post.domain.History;

public interface HistoryQueryService {
    History getHistory(Long postId);
}
