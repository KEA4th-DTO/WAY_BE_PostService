package com.dto.way.post.web.controller;

import com.dto.way.post.converter.DailyConverter;
import com.dto.way.post.domain.Daily;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.dailyService.DailyCommandService;
import com.dto.way.post.service.dailyService.DailyQueryService;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/daily-service")
@RequiredArgsConstructor
public class DailyRestController {

    private final DailyCommandService dailyCommandService;
    private final DailyQueryService dailyQueryService;

    @PostMapping
    public ApiResponse<DailyResponseDto.CreateDailyResultDto> createDaily(@RequestPart(value = "image", required = true) MultipartFile image,
                                                                          @RequestPart(value = "createDailyDto") DailyRequestDto.CreateDailyDto request) throws ParseException {
        Daily daily = dailyCommandService.createDaily(image, request);
        return ApiResponse.of(SuccessStatus.DAILY_CREATED, DailyConverter.toCreateDailyResultDto(daily));
    }


    @PatchMapping("/{postId}")
    public ApiResponse<DailyResponseDto.UpdateDailyResultDto> updateDaily(@PathVariable(name = "postId") Long postId, @RequestBody DailyRequestDto.UpdateDailyDto request) {

        Daily daily = dailyCommandService.updateDaily(postId, request);
        return ApiResponse.of(SuccessStatus.DAILY_UPDATED, DailyConverter.toUpdateDailyResponseDto(daily));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<DailyResponseDto.DeleteDailyResultDto> deleteDaily(@PathVariable(name = "postId") Long postId) throws IOException {
        return ApiResponse.of(SuccessStatus.DAILY_DELETED, dailyCommandService.deleteDaily(postId));
    }

    @GetMapping("/{postId}")
    public ApiResponse<DailyResponseDto.GetDailyResultDto> getDaily(@PathVariable(name = "postId") Long postId) throws IOException {
        Daily daily = dailyQueryService.getDaily(postId);
        return ApiResponse.of(SuccessStatus.DAILY_FOUND, DailyConverter.toGetDailyResponseDto(daily));
    }

}
