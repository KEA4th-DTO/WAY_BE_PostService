package com.dto.way.post.web.dto.reportDto;

import com.dto.way.post.domain.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportRequestDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateReportDto {
        private ReportType type;
        private String description;
    }
}
