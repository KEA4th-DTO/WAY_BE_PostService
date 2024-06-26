package com.dto.way.post.converter;

import com.dto.way.post.domain.Report;
import com.dto.way.post.domain.enums.ReportStatus;
import com.dto.way.post.web.dto.reportDto.ReportRequestDto;
import com.dto.way.post.web.dto.reportDto.ReportResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class ReportConverter {


    public static Report toReport(Long memberId, Long targetId, ReportRequestDto.CreateReportDto request) {
        return Report.builder()
                .status(ReportStatus.PROCESS)
                .title(request.getTitle())
                .targetId(targetId)
                .type(request.getType())
                .memberId(memberId)
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

    public static ReportResponseDto.ChangeReportStatusResultDto toChangeReportStatusResultDto(Report report) {
        return ReportResponseDto.ChangeReportStatusResultDto.builder()
                .reportId(report.getId())
                .status(report.getStatus())
                .title(report.getTitle())
                .type(report.getType())
                .build();
    }

    public static ReportResponseDto.GetReportResultDto toGetReportResultDto(Report report) {
        return ReportResponseDto.GetReportResultDto.builder()
                .reportId(report.getId())
                .title(report.getTitle())
                .type(report.getType())
                .status(report.getStatus())
                .targetId(report.getTargetId())
                .reportedAt(report.getCreatedAt()).build();
    }

    public static List<ReportResponseDto.GetReportResultDto> toGetReportResultDtoList(List<Report> reportList) {
        return reportList.stream().map(ReportConverter::toGetReportResultDto).collect(Collectors.toList());
    }

}
