package com.dto.way.post.converter;

import com.dto.way.post.domain.Report;
import com.dto.way.post.domain.enums.ReportStatus;
import com.dto.way.post.web.dto.reportDto.ReportRequestDto;
import com.dto.way.post.web.dto.reportDto.ReportResponseDto;

public class ReportConverter {


    public static Report toReport(String email, Long targetId, ReportRequestDto.CreateReportDto request) {
        return Report.builder()
                .status(ReportStatus.PROCESS)
                .title(request.getTitle())
                .targetId(targetId)
                .type(request.getType())
                .memberEmail(email)
                .description(request.getDescription()).build();
    }


    public static ReportResponseDto.CreateReportResultDto toCreateReportResultDto(Report report) {
        return ReportResponseDto.CreateReportResultDto.builder()
                .reportId(report.getId())
                .title(report.getTitle())
                .type(report.getType())
                .status(report.getStatus())
                .targetId(report.getTargetId())
                .reportedAt(report.getCreatedAt()).build();
    }

}
