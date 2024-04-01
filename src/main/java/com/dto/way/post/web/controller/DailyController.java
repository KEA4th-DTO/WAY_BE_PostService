package com.dto.way.post.web.controller;

import com.dto.way.post.converter.DailyConverter;
import com.dto.way.post.domain.Daily;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.repository.DailyRepository;
import com.dto.way.post.service.DailyCommandService;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/post-service")
@RequiredArgsConstructor
public class DailyController {

    private final DailyCommandService dailyCommandService;

    @PostMapping
    public ApiResponse<DailyResponseDto.CreateDailyResultDto> createDaily(@RequestPart(value="image",required = true) MultipartFile image,
                                                                          @RequestPart(value="createDailyDto") DailyRequestDto.CreateDailyDto request) {
        Daily daily = dailyCommandService.createDaily(image, request);

        return ApiResponse.of(SuccessStatus.DAILY_CREATED, DailyConverter.toCreateDailyResultDto(daily));
    }
}
