package com.dto.way.post.service.reportService;

import com.dto.way.post.domain.Report;
import com.dto.way.post.web.dto.reportDto.ReportRequestDto;
import com.dto.way.post.web.dto.reportDto.ReportResponseDto;
import org.springframework.security.core.Authentication;

public interface ReportCommandService {

    Report createReport(Authentication auth, Long targetId, ReportRequestDto.CreateReportDto request);
}
