package com.dto.way.post.service;

import com.dto.way.post.domain.Daily;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface DailyCommandService {
    Daily createDaily(MultipartFile image, DailyRequestDto.CreateDailyDto requestDto);   //  회원 서비스 구현 완료시 수정사항 있음

}
