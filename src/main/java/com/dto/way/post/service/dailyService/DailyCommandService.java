package com.dto.way.post.service.dailyService;

import com.dto.way.post.domain.Daily;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.locationtech.jts.io.ParseException;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DailyCommandService {
    DailyResponseDto.CreateDailyResultDto createDaily(HttpServletRequest httpServletRequest, MultipartFile image, DailyRequestDto.CreateDailyDto requestDto) throws ParseException;   //  회원 서비스 구현 완료시 수정사항 있음

    Daily updateDaily(HttpServletRequest httpServletRequest, Long dailyId, DailyRequestDto.UpdateDailyDto requestDto);

    DailyResponseDto.DeleteDailyResultDto deleteDaily(HttpServletRequest httpServletRequest, Long dailyId) throws IOException;

    void changePostStatus();

}
