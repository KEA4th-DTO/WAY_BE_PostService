package com.dto.way.post.service.reportService;

import com.dto.way.post.domain.Report;
import com.dto.way.post.domain.enums.ReportStatus;
import com.dto.way.post.web.dto.reportDto.ReportResponseDto;

import java.util.List;

public interface ReportQueryService {
    List<ReportResponseDto.GetReportResultDto> getReportWithStatus(ReportStatus reportStatus);

}
