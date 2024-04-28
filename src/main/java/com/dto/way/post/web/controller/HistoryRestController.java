package com.dto.way.post.web.controller;

import com.dto.way.post.converter.HistoryConverter;
import com.dto.way.post.domain.History;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.historyService.HistoryCommandService;
import com.dto.way.post.web.dto.historyDto.HistoryRequestDto;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/history-service")
public class HistoryRestController {
    private final HistoryCommandService historyCommandService;

    @PostMapping
    public ApiResponse<HistoryResponseDto.CreateHistoryResultDto> createHistory(@RequestPart(value = "image",required = true) MultipartFile thumbnailImage,
                                                                                @RequestPart(value = "html",required = true) MultipartFile bodyHtml,
                                                                                @RequestPart(value = "createHistoryDto", required = true)HistoryRequestDto.CreateHistoryDto request) throws ParseException {
        History history = historyCommandService.createHistory(thumbnailImage, bodyHtml, request);
        return ApiResponse.of(SuccessStatus.HISTORY_CREATED, HistoryConverter.toCreateHistoryResponseDto(history));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<HistoryResponseDto.DeleteHistoryResultDto> deleteHistory(@PathVariable(name = "postId") Long postId) throws IOException {
        return ApiResponse.of(SuccessStatus.HISTORY_DELETED, historyCommandService.deleteHistory(postId));
    }
}
