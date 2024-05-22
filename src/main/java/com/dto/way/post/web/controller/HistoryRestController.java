package com.dto.way.post.web.controller;

import com.dto.way.post.converter.HistoryConverter;
import com.dto.way.post.domain.History;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.commentService.CommentCommandService;
import com.dto.way.post.service.historyService.HistoryCommandService;
import com.dto.way.post.service.historyService.HistoryQueryService;
import com.dto.way.post.web.dto.historyDto.HistoryRequestDto;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/post-service/history")
public class HistoryRestController {
    private final HistoryCommandService historyCommandService;
    private final HistoryQueryService historyQueryService;
    private final CommentCommandService commentCommandService;

    @PostMapping
    public ApiResponse<HistoryResponseDto.CreateHistoryResultDto> createHistory(Authentication auth,
                                                                                @RequestPart(value = "image", required = true) MultipartFile thumbnailImage,
                                                                                @RequestPart(value = "html", required = true) MultipartFile bodyHtml,
                                                                                @Valid @RequestPart(value = "createHistoryDto", required = true) HistoryRequestDto.CreateHistoryDto request) throws ParseException {
        History history = historyCommandService.createHistory(auth, thumbnailImage, bodyHtml, request);
        return ApiResponse.of(SuccessStatus.HISTORY_CREATED, HistoryConverter.toCreateHistoryResponseDto(history));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<HistoryResponseDto.DeleteHistoryResultDto> deleteHistory(Authentication auth,
                                                                                @PathVariable(name = "postId") Long postId) throws IOException {
        return ApiResponse.of(SuccessStatus.HISTORY_DELETED, historyCommandService.deleteHistory(auth, postId));
    }

    @GetMapping("/{postId}")
    public ApiResponse<HistoryResponseDto.GetHistoryResultDto> getHistory(@PathVariable(name = "postId") Long postId) {

        History history = historyQueryService.getHistory(postId);
        HistoryResponseDto.GetHistoryResultDto getHistoryResultDto = HistoryConverter.toGetHistoryResultDto(history, commentCommandService.countComment(postId));
        return ApiResponse.of(SuccessStatus.HISTORY_FOUND, getHistoryResultDto);
    }

    @GetMapping("/list")
    public ApiResponse<HistoryResponseDto.GetHistoryListResultDto> getHistoryList(Authentication auth,
                                                                                  @RequestParam Double latitude1,
                                                                                  @RequestParam Double longitude1,
                                                                                  @RequestParam Double latitude2,
                                                                                  @RequestParam Double longitude2) {

        HistoryResponseDto.GetHistoryListResultDto historyDtoList = historyQueryService.getHistoryListByRange(auth, latitude1, longitude1, latitude2, longitude2);
        return ApiResponse.of(SuccessStatus.HISTORY_LIST_FOUND_BY_RANGE, historyDtoList);
    }
}
