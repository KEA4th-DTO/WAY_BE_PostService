package com.dto.way.post.web.dto.reportDto;

import com.dto.way.post.domain.enums.ReportStatus;
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
        private String title;
        private ReportType type;
        private ReportStatus status;
        private Long targetId;
        private LocalDateTime reportedAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangeReportStatusResultDto {
        private Long reportId;
        private String title;
        private ReportType type;
        private ReportStatus status;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetReportResultDto {
        private Long reportId;
        private String title;
        private ReportType type;
        private ReportStatus status;
        private Long targetId;
        private LocalDateTime reportedAt;
    }
}
