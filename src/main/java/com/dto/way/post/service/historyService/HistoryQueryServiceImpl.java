package com.dto.way.post.service.historyService;

import com.dto.way.post.domain.History;
import com.dto.way.post.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryQueryServiceImpl implements HistoryQueryService{

    private final HistoryRepository historyRepository;
    
    @Override
    public History getHistory(Long postId) {

        History history = historyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 히스토리가 존재하지 않습니다."));
        return history;
    }
}
