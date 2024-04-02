package com.dto.way.post.service;

import com.dto.way.post.domain.Daily;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DailyCommandService {
    Daily createDaily(MultipartFile image, DailyRequestDto.CreateDailyDto requestDto);   //  회원 서비스 구현 완료시 수정사항 있음

    Daily updateDaily(Long dailyId, DailyRequestDto.UpdateDailyDto requestDto);

    DailyResponseDto.DeleteDailyResultDto deleteDaily(Long dailyId) throws IOException;
}
