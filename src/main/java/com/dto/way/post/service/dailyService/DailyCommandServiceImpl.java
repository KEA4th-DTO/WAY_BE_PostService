package com.dto.way.post.service.dailyService;

import com.dto.way.post.aws.s3.AmazonS3Manager;
import com.dto.way.post.aws.s3.S3FileService;
import com.dto.way.post.converter.DailyConverter;
import com.dto.way.post.domain.Daily;
import com.dto.way.post.domain.Post;
import com.dto.way.post.aws.config.AmazonConfig;
import com.dto.way.post.domain.enums.Expiration;
import com.dto.way.post.domain.enums.PostType;
import com.dto.way.post.global.exception.ExceptionHandler;
import com.dto.way.post.global.response.code.status.ErrorStatus;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.DailyRepository;
import com.dto.way.post.repository.PostRepository;
import com.dto.way.post.utils.UuidCreator;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DailyCommandServiceImpl implements DailyCommandService {

    private final DailyRepository dailyRepository;
    private final PostRepository postRepository;
    private final AmazonS3Manager s3Manager;
    private final S3FileService s3FileService;
    private final AmazonConfig amazonConfig;
    private final UuidCreator uuidCreator;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public DailyResponseDto.CreateDailyResultDto createDaily(HttpServletRequest httpServletRequest, MultipartFile image, DailyRequestDto.CreateDailyDto requestDto) throws ParseException {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        String imageUrl = s3Manager.uploadFileToDirectory(amazonConfig.getDailyImagePath(), uuidCreator.createUuid(), image);

        //  AI 분석 데이터를 위해 daily 본문 내용을 S3 파일에 저장한다.
        CompletableFuture<String> future = s3FileService.saveOrUpdateFileAsync(requestDto.getBody(), amazonConfig.getAiText() + "/"+"text_member_id_" + loginMemberId);

        // 위도, 경도를 Point로 변환하여 저장.
        Double latitude = requestDto.getLatitude();
        Double longitude = requestDto.getLongitude();
        Point point = latitude != null && longitude != null ?
                (Point) new WKTReader().read(String.format("POINT(%s %s)", latitude, longitude))
                : null;

        Daily daily = DailyConverter.toDaily(point, imageUrl, requestDto);
        Post post = daily.getPost();

        post.setMemberId(loginMemberId);

        Daily createDaily = dailyRepository.save(daily);
        Long count = postRepository.countByMemberId(loginMemberId);
        boolean captureFlag = (count > 0) && (count % 15 == 0);

        return DailyResponseDto.CreateDailyResultDto.builder()
                .postType(PostType.DAILY)
                .postId(createDaily.getPostId())
                .title(createDaily.getTitle())
                .createdAt(createDaily.getCreatedAt())
                .capture(captureFlag)
                .build();
    }

    @Override
    @Transactional
    public Daily updateDaily(HttpServletRequest httpServletRequest, Long postId, DailyRequestDto.UpdateDailyDto requestDto) {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        Daily daily = dailyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 데일리가 존재하지 않습니다."));

        if (loginMemberId.equals(daily.getPost().getMemberId())) {
            //  작성자와 사용자가 같으면 수정 가능
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
        } else {
            //  작성자와 사용자가 다르면 예외 처리
            throw new SecurityException("게시글은 작성자만 수정할 수 있습니다.");
        }


        return daily;
    }

    @Override
    @Transactional
    public DailyResponseDto.DeleteDailyResultDto deleteDaily(HttpServletRequest httpServletRequest, Long postId) throws IOException {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        Daily daily = dailyRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.DAILY_NOT_FOUND));
        DailyResponseDto.DeleteDailyResultDto deleteDailyResultDto = DailyConverter.toDeleteDailyResponseDto(daily);

        if (loginMemberId.equals(daily.getPost().getMemberId())) {
            //  사용자와 작성자가 같으면 삭제 가능
            s3Manager.deleteFile(daily.getImageUrl());
            dailyRepository.delete(daily);
        } else {
            //  사용자와 작성자가 다르면 예외처리
            throw new ExceptionHandler(ErrorStatus._FORBIDDEN);
        }

        return deleteDailyResultDto;
    }

    @Override
    @Transactional
    @Async
    @Scheduled(cron = "0 */10 * * * *") //  10분 단위로 Daily의 만료 날짜를 확인해서 상태를 변경한다.
    public void changePostStatus() {
        List<Daily> dailyList = dailyRepository.findByExpiredAtBefore(LocalDateTime.now());

        dailyList.forEach(daily -> {
            daily.getPost().updateExpiration(Expiration.EXPIRED);
        });
    }
}
