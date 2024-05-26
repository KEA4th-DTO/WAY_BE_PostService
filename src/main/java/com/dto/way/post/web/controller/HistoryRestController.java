package com.dto.way.post.web.controller;

import com.dto.way.post.converter.HistoryConverter;
import com.dto.way.post.domain.History;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.historyService.HistoryCommandService;
import com.dto.way.post.service.historyService.HistoryQueryService;
import com.dto.way.post.web.dto.historyDto.HistoryRequestDto;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post-service/history")
public class HistoryRestController {
    private final HistoryCommandService historyCommandService;
    private final HistoryQueryService historyQueryService;


    @Operation(summary = "History 게시글 생성 API", description = "form-data 형식으로 썸네일 이미지(image), 본문 html(bodyHtml), createHistoryDto을 전송해주세요.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<HistoryResponseDto.CreateHistoryResultDto> createHistory(HttpServletRequest httpServletRequest,
                                                                                @RequestPart(value = "image", required = true) MultipartFile thumbnailImage,
                                                                                @RequestPart(value = "html", required = true) MultipartFile bodyHtml,
                                                                                @Valid @RequestPart(value = "createHistoryDto", required = true) HistoryRequestDto.CreateHistoryDto request) throws ParseException {
        History history = historyCommandService.createHistory(httpServletRequest, thumbnailImage, bodyHtml, request);
        return ApiResponse.of(SuccessStatus.HISTORY_CREATED, HistoryConverter.toCreateHistoryResponseDto(history));
    }

    @Operation(summary = "History 게시글 삭제 API", description = "PathVariable 으로 삭제할 History postId를 전송해주세요.")
    @DeleteMapping("/{postId}")
    public ApiResponse<HistoryResponseDto.DeleteHistoryResultDto> deleteHistory(HttpServletRequest httpServletRequest,
                                                                                @PathVariable(name = "postId") Long postId) throws IOException {
        return ApiResponse.of(SuccessStatus.HISTORY_DELETED, historyCommandService.deleteHistory(httpServletRequest, postId));
    }

    @Operation(summary = "History 게시글 수정 API", description = "form-data 형식으로 수정할 썸네일 이미지(image), 본문 html(bodyHtml), updateHistoryDto을 전송해주세요.")
    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<HistoryResponseDto.UpdateHistoryResultDto> updateHistory(HttpServletRequest httpServletRequest,
                                                                                @PathVariable(name = "postId") Long postId,
                                                                                @RequestPart(value = "image", required = true) MultipartFile thumbnailImage,
                                                                                @RequestPart(value = "html", required = true) MultipartFile bodyHtml,
                                                                                @Valid @RequestPart(value = "updateHistoryDto", required = true) HistoryRequestDto.UpdateHistoryDto request) throws IOException {
        History history = historyCommandService.updateHistory(httpServletRequest, postId, thumbnailImage, bodyHtml, request);
        return ApiResponse.of(SuccessStatus.HISTORY_UPDATE, HistoryConverter.toUpdateHistoryResponseDto(history));
    }

    @Operation(summary = "History 게시글 상세 조회(단건) API", description = "PathVariable 으로 조회할 History postId를 전송해주세요.")
    @GetMapping("/{postId}")
    public ApiResponse<HistoryResponseDto.GetHistoryResultDto> getHistory(HttpServletRequest httpServletRequest,
                                                                          @PathVariable(name = "postId") Long postId) {

        HistoryResponseDto.GetHistoryResultDto getHistoryResultDto = historyQueryService.getHistoryResult(httpServletRequest, postId);
        return ApiResponse.of(SuccessStatus.HISTORY_FOUND, getHistoryResultDto);
    }

    @Operation(summary = "History 게시글 반경 조회 API", description = "RequestParam 형식으로 지도의 좌하단 좌표, 우상단 좌표를 전송해주세요.")
    @GetMapping("/list")
    public ApiResponse<HistoryResponseDto.GetHistoryListResultDto> getHistoryList(HttpServletRequest httpServletRequest,
                                                                                  @RequestParam Double latitude1,
                                                                                  @RequestParam Double longitude1,
                                                                                  @RequestParam Double latitude2,
                                                                                  @RequestParam Double longitude2) {

        HistoryResponseDto.GetHistoryListResultDto historyDtoList = historyQueryService.getHistoryListByRange(httpServletRequest, latitude1, longitude1, latitude2, longitude2);
        return ApiResponse.of(SuccessStatus.HISTORY_LIST_FOUND_BY_RANGE, historyDtoList);
    }

    @Operation(summary = "History 이미지를 url로 변환하는 API",description = "Requestbody의 form-data 형식으로 변환할 이미지 파일을 전송해주세요. ")
    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> getImageUrlFromS3(@RequestParam(name = "historyImage") MultipartFile historyImage) {

        String historyImageUrl = historyCommandService.historyImageUrl(historyImage);
        return ApiResponse.of(SuccessStatus.HISTORY_IMAGE_URL,historyImageUrl);
    }

}
