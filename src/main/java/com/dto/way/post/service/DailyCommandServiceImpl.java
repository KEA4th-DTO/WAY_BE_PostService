package com.dto.way.post.service;

import com.dto.way.post.aws.s3.AmazonS3Manager;
import com.dto.way.post.converter.DailyConverter;
import com.dto.way.post.domain.Daily;
import com.dto.way.post.domain.common.Uuid;
import com.dto.way.post.repository.DailyRepository;
import com.dto.way.post.repository.UuidRepository;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DailyCommandServiceImpl implements DailyCommandService{

    private final DailyRepository dailyRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;

    @Override
    public Daily createDaily(MultipartFile image, DailyRequestDto.CreateDailyDto requestDto) {
        /**
         *  회원 서비스 구현 완료되면 memberId는 JWT claim에서 가져오는 것으로 변경 필요
         *  memberId 가져온 후 userService로 api 날려서 조회 로직
         */

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder()
                .uuid(uuid)
                .build());

        String imageUrl = s3Manager.uploadFile(s3Manager.generateReviewKeyName(savedUuid), image);

        Daily daily = DailyConverter.toDaily(imageUrl,requestDto);

        //  여기서 member 유효성 검증 처리하던지, setMemberId에서 처리하던지 -> 회원서비스 구현 후 처리
        daily.setMemberId(1L);

        return dailyRepository.save(daily);
    }
}
