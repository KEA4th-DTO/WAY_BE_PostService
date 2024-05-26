package com.dto.way.post.web.controller;

import com.dto.way.post.converter.DailyConverter;
import com.dto.way.post.domain.Daily;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.dailyService.DailyCommandService;
import com.dto.way.post.service.dailyService.DailyQueryService;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/post-service/daily")
@RequiredArgsConstructor
public class DailyRestController {

    private final DailyCommandService dailyCommandService;
    private final DailyQueryService dailyQueryService;

    @Operation(summary = "Daily 게시글 생성 API", description = "form-data 형식으로 image와 createDailyDto을 전송해주세요.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<DailyResponseDto.CreateDailyResultDto> createDaily(HttpServletRequest httpServletRequest,
                                                                          @Valid @RequestPart(value = "image", required = true) MultipartFile image,
                                                                          @Valid @RequestPart(value = "createDailyDto") DailyRequestDto.CreateDailyDto request) throws ParseException {
        Daily daily = dailyCommandService.createDaily(httpServletRequest, image, request);
        return ApiResponse.of(SuccessStatus.DAILY_CREATED, DailyConverter.toCreateDailyResultDto(daily));
    }

    @Operation(summary = "Daily 게시글 수정 API", description = "PathVariable 형식으로 수정할 Daily 게시글 postId, RequestBody로 수정 내역을 전송해주세요.")
    @PatchMapping("/{postId}")
    public ApiResponse<DailyResponseDto.UpdateDailyResultDto> updateDaily(HttpServletRequest httpServletRequest,
                                                                          @PathVariable(name = "postId") Long postId,
                                                                          @Valid @RequestBody DailyRequestDto.UpdateDailyDto request) {

        Daily daily = dailyCommandService.updateDaily(httpServletRequest, postId, request);
        return ApiResponse.of(SuccessStatus.DAILY_UPDATED, DailyConverter.toUpdateDailyResponseDto(daily));
    }


    @Operation(summary = "Daily 게시글 삭제 API", description = "PathVariable 형식으로 삭제할 Daily 게시글 postId를 전송해주세요.")
    @DeleteMapping("/{postId}")
    public ApiResponse<DailyResponseDto.DeleteDailyResultDto> deleteDaily(HttpServletRequest httpServletRequest,
                                                                          @PathVariable(name = "postId") Long postId) throws IOException {
        return ApiResponse.of(SuccessStatus.DAILY_DELETED, dailyCommandService.deleteDaily(httpServletRequest, postId));
    }


    @Operation(summary = "Daily 게시글 상세 조회(단건) API", description = "PathVariable 형식으로 상세조회할 Daily 게시글 postId를 전송해주세요.")
    @GetMapping("/{postId}")
    public ApiResponse<DailyResponseDto.GetDailyResultDto> getDaily(HttpServletRequest httpServletRequest, @PathVariable(name = "postId") Long postId) throws IOException {
        DailyResponseDto.GetDailyResultDto  getDailyResultDto= dailyQueryService.getDailyResultDto(httpServletRequest, postId);
        return ApiResponse.of(SuccessStatus.DAILY_FOUND,getDailyResultDto);
    }


    // 반경 내 데일리 목록 조회 API
    @Operation(summary = "Daily 게시글 반경 조회 API", description = "RequestParam 형식으로 지도의 좌하단 좌표, 우상단 좌표를 전송해주세요.")
    @GetMapping("/list")
    public ApiResponse<DailyResponseDto.GetDailyListResultDto> getDailyList(HttpServletRequest httpServletRequest,
                                                                            @RequestParam Double latitude1,
                                                                            @RequestParam Double longitude1,
                                                                            @RequestParam Double latitude2,
                                                                            @RequestParam Double longitude2) {

        DailyResponseDto.GetDailyListResultDto dailyList = dailyQueryService.getDailyListByRange(httpServletRequest, latitude1, longitude1, latitude2, longitude2);
        return ApiResponse.of(SuccessStatus.DAILY_LIST_FOUND_BY_RANGE, dailyList);
    }
}
