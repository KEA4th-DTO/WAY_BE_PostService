package com.dto.way.post.service.reportService;

import com.dto.way.post.domain.Report;
import com.dto.way.post.domain.enums.ReportStatus;
import com.dto.way.post.web.dto.reportDto.ReportRequestDto;
import com.dto.way.post.web.dto.reportDto.ReportResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface ReportCommandService {

    Report createReport(HttpServletRequest httpServletRequest, Long targetId, ReportRequestDto.CreateReportDto request);

    ReportResponseDto.ChangeReportStatusResultDto changeReportStatus(Long reportId, ReportStatus reportStatus);
}
