package com.dto.way.post.web.dto.historyDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class HistoryResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateHistoryResponseDto {
        private Long postId;
        private String title;
        private LocalDateTime createAt;
    }
}
