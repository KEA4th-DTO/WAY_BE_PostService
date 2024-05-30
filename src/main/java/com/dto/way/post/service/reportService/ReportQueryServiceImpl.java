package com.dto.way.post.service.reportService;

import com.dto.way.post.converter.ReportConverter;
import com.dto.way.post.domain.Report;
import com.dto.way.post.domain.enums.ReportStatus;
import com.dto.way.post.repository.ReportRepository;
import com.dto.way.post.web.dto.reportDto.ReportResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportQueryServiceImpl implements ReportQueryService{

    private final ReportRepository reportRepository;

    @Override
    public List<ReportResponseDto.GetReportResultDto> getReportWithStatus(ReportStatus reportStatus) {
        List<Report> reportList = reportRepository.findByStatus(reportStatus);

        return ReportConverter.toGetReportResultDtoList(reportList);
    }
}
