package com.dto.way.post.service.dailyService;

import com.dto.way.post.aws.s3.AmazonS3Manager;
import com.dto.way.post.converter.DailyConverter;
import com.dto.way.post.domain.Daily;
import com.dto.way.post.domain.Post;
import com.dto.way.post.domain.common.Uuid;
import com.dto.way.post.global.config.AmazonConfig;
import com.dto.way.post.repository.DailyRepository;
import com.dto.way.post.repository.UuidRepository;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DailyCommandServiceImpl implements DailyCommandService {

    private final DailyRepository dailyRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final AmazonConfig amazonConfig;

    @Override
    @Transactional
    public Daily createDaily(MultipartFile image, DailyRequestDto.CreateDailyDto requestDto) throws ParseException {
        /**
         *  회원 서비스 구현 완료되면 memberId는 JWT claim에서 가져오는 것으로 변경 필요
         *  memberId 가져온 후 userService로 api 날려서 조회 로직
         */
        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder()
                .uuid(uuid)
                .build());

        String imageUrl = s3Manager.uploadFileToDirectory(amazonConfig.getDailyImagePath(), savedUuid.getUuid(), image);


        // 위도, 경도를 Point로 변환하여 저장.
        Double latitude = requestDto.getLatitude();
        Double longitude = requestDto.getLongitude();
        Point point = latitude != null && longitude != null ?
                (Point) new WKTReader().read(String.format("POINT(%s %s)", latitude, longitude))
                : null;

        Daily daily = DailyConverter.toDaily(point, imageUrl, requestDto);
        Post post = daily.getPost();

        //  여기서 member 유효성 검증 처리하던지, setMemberId에서 처리하던지 -> 회원서비스 구현 후 처리
        post.setMemberId(1L);

        return dailyRepository.save(daily);
    }

    @Override
    @Transactional
    public Daily updateDaily(Long postId, DailyRequestDto.UpdateDailyDto requestDto) {

        //  member 유효성 검사 추가 필요
        Daily daily = dailyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 데일리가 존재하지 않습니다."));
        if (daily.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new DateTimeException("만료시간이 지난 게시글은 수정할 수 없습니다.");
        } else {
            if (requestDto.getTitle() != null) {
                daily.updateTitle(requestDto.getTitle());
            }
            if (requestDto.getBody() != null) {
                daily.updateBody(requestDto.getBody());
            }
        }

        return daily;
    }

    @Override
    public DailyResponseDto.DeleteDailyResultDto deleteDaily(Long postId) throws IOException {

        Daily daily = dailyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("데일리가 존재하지 않습니다."));
        s3Manager.deleteFile(amazonConfig.getDailyImagePath(), daily.getImageUrl());
        DailyResponseDto.DeleteDailyResultDto deleteDailyResultDto = DailyConverter.toDeleteDailyResponseDto(daily);

        dailyRepository.delete(daily);

        return deleteDailyResultDto;
    }
}
