package com.dto.way.post.web.controller;


import com.dto.way.post.converter.ReportConverter;
import com.dto.way.post.domain.Report;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.reportService.ReportCommandService;
import com.dto.way.post.web.dto.reportDto.ReportRequestDto;
import com.dto.way.post.web.dto.reportDto.ReportResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post-service/report")
public class ReportRestController {

    private final ReportCommandService reportCommandService;

    @PostMapping("/{targetId}")
    public ApiResponse<ReportResponseDto.CreateReportResultDto> createReport(Authentication auth,
                                                                             @PathVariable(name = "targetId") Long targetId,
                                                                             @RequestBody ReportRequestDto.CreateReportDto request) {
        Report report = reportCommandService.createReport(auth, targetId, request);
        return ApiResponse.of(SuccessStatus.REPORT_CREATED, ReportConverter.toCreateReportResultDto(report));
    }
}
