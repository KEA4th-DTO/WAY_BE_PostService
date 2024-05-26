package com.dto.way.post.web.controller;


import com.dto.way.post.converter.ReportConverter;
import com.dto.way.post.domain.Report;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.reportService.ReportCommandService;
import com.dto.way.post.web.dto.reportDto.ReportRequestDto;
import com.dto.way.post.web.dto.reportDto.ReportResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post-service/report")
public class ReportRestController {

    private final ReportCommandService reportCommandService;

    @Operation(summary = "신고 API", description = "게시글, 댓글, 대댓글을 신고하는 API 입니다. PathVariable 으로 신고할 대상의 id(postId or commentId or replyId), RequestBody 으로 신고 내용을 전송해주세요. ")
    @PostMapping("/{targetId}")
    public ApiResponse<ReportResponseDto.CreateReportResultDto> createReport(HttpServletRequest httpServletRequest,
                                                                             @PathVariable(name = "targetId") Long targetId,
                                                                             @Valid @RequestBody ReportRequestDto.CreateReportDto request) {
        Report report = reportCommandService.createReport(httpServletRequest, targetId, request);
        return ApiResponse.of(SuccessStatus.REPORT_CREATED, ReportConverter.toCreateReportResultDto(report));
    }
}
