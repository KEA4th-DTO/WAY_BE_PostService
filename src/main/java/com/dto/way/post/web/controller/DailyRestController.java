package com.dto.way.post.web.controller;

import com.dto.way.post.converter.DailyConverter;
import com.dto.way.post.domain.Daily;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.dailyService.DailyCommandService;
import com.dto.way.post.service.dailyService.DailyQueryService;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
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

    /**
     * 데일리 생성 API
     *
     * @param image
     * @param request
     * @return
     * @throws ParseException
     */
    @PostMapping
    public ApiResponse<DailyResponseDto.CreateDailyResultDto> createDaily(Authentication auth,
                                                                          @Valid @RequestPart(value = "image", required = true) MultipartFile image,
                                                                          @Valid @RequestPart(value = "createDailyDto") DailyRequestDto.CreateDailyDto request) throws ParseException {
        Daily daily = dailyCommandService.createDaily(auth, image, request);
        return ApiResponse.of(SuccessStatus.DAILY_CREATED, DailyConverter.toCreateDailyResultDto(daily));
    }

    /**
     * 데일리 수정 API
     *
     * @param postId
     * @param request
     * @return
     */
    @PatchMapping("/{postId}")
    public ApiResponse<DailyResponseDto.UpdateDailyResultDto> updateDaily(Authentication auth,
                                                                          @PathVariable(name = "postId") Long postId,
                                                                          @Valid @RequestBody DailyRequestDto.UpdateDailyDto request) {

        Daily daily = dailyCommandService.updateDaily(auth, postId, request);
        return ApiResponse.of(SuccessStatus.DAILY_UPDATED, DailyConverter.toUpdateDailyResponseDto(daily));
    }

    /**
     * 데일리 삭제 API
     *
     * @param postId
     * @return
     * @throws IOException
     */
    @DeleteMapping("/{postId}")
    public ApiResponse<DailyResponseDto.DeleteDailyResultDto> deleteDaily(Authentication auth,
                                                                          @PathVariable(name = "postId") Long postId) throws IOException {
        return ApiResponse.of(SuccessStatus.DAILY_DELETED, dailyCommandService.deleteDaily(auth, postId));
    }

    /**
     * 데일리 단건 조회 API
     *
     * @param postId
     * @return
     * @throws IOException
     */
    @GetMapping("/{postId}")
    public ApiResponse<DailyResponseDto.GetDailyResultDto> getDaily(@PathVariable(name = "postId") Long postId) throws IOException {
        Daily daily = dailyQueryService.getDaily(postId);
        return ApiResponse.of(SuccessStatus.DAILY_FOUND, DailyConverter.toGetDailyResponseDto(daily));
    }


    // 반경 내 데일리 목록 조회 API
    @GetMapping("/posts")
    public ApiResponse<DailyResponseDto.GetDailyListResultDto> getDailyList(@RequestParam Double latitude1,
                                                                            @RequestParam Double longitude1,
                                                                            @RequestParam Double latitude2,
                                                                            @RequestParam Double longitude2) {

        DailyResponseDto.GetDailyListResultDto dailyList = dailyQueryService.getDailyListByRange(latitude1, longitude1, latitude2, longitude2);
        return ApiResponse.of(SuccessStatus.PINS_FOUND_BY_RANGE, dailyList);
    }
}
