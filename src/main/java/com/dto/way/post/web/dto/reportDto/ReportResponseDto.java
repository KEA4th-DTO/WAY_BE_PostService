package com.dto.way.post.web.dto.reportDto;

import com.dto.way.post.domain.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReportResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateReportResultDto {
        private Long reportId;
        private ReportType type;
        private Long targetId;
        private LocalDateTime reportedAt;

    }
}
