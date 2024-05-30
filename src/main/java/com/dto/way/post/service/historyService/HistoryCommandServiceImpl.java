package com.dto.way.post.service.historyService;

import com.dto.way.post.aws.s3.AmazonS3Manager;
import com.dto.way.post.aws.s3.S3FileService;
import com.dto.way.post.converter.HistoryConverter;
import com.dto.way.post.domain.History;
import com.dto.way.post.domain.Post;
import com.dto.way.post.aws.config.AmazonConfig;
import com.dto.way.post.domain.enums.PostType;
import com.dto.way.post.global.exception.ExceptionHandler;
import com.dto.way.post.global.response.code.status.ErrorStatus;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.HistoryRepository;
import com.dto.way.post.repository.PostRepository;
import com.dto.way.post.utils.UuidCreator;
import com.dto.way.post.web.dto.historyDto.HistoryRequestDto;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryCommandServiceImpl implements HistoryCommandService {

    private final HistoryRepository historyRepository;
    private final PostRepository postRepository;
    private final AmazonConfig amazonConfig;
    private final AmazonS3Manager s3Manager;
    private final S3FileService s3FileService;
    private final UuidCreator uuidCreator;
    private final JwtUtils jwtUtils;


    @Override
    @Transactional
    public HistoryResponseDto.CreateHistoryResultDto createHistory(HttpServletRequest httpServletRequest, MultipartFile thumbnailImage, HistoryRequestDto.CreateHistoryDto createHistoryDto) throws ParseException {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);


        //  AI 분석 데이터를 위해 html 태그가 전부 빠진 history 본문 내용을 S3 파일에 저장한다.
        CompletableFuture<String> future = s3FileService.saveOrUpdateFileAsync(createHistoryDto.getBodyPlainText(), amazonConfig.getAiText() + "/" + "text_member_id_" + loginMemberId);

        // 비동기 작업이 완료된 후 추가 작업 수행
//        future.thenAccept(log::info)
//                .exceptionally(ex -> {
//                    System.err.println("Failed to complete async operation: " + ex.getMessage());
//                    return null;
//                });

        String thumbnailImageUrl = s3Manager.uploadFileToDirectory(amazonConfig.getHistoryThumbnailPath(), uuidCreator.createUuid(), thumbnailImage);

        Double latitude = createHistoryDto.getLatitude();
        Double longitude = createHistoryDto.getLongitude();
        Point point = latitude != null && longitude != null ?
                (Point) new WKTReader().read(String.format("POINT(%s %s)", latitude, longitude))
                : null;

        History history = HistoryConverter.toHistory(point, thumbnailImageUrl, createHistoryDto);
        Post post = history.getPost();

        post.setMemberId(loginMemberId);

        History createdHistory = historyRepository.save(history);
        Long count = postRepository.countByMemberId(loginMemberId);

        boolean captureFlag = (count > 0) && (count % 15 == 0);

        return HistoryResponseDto.CreateHistoryResultDto.builder()
                .postType(PostType.HISTORY)
                .postId(createdHistory.getPostId())
                .title(createdHistory.getTitle())
                .createAt(createdHistory.getCreatedAt())
                .capture(captureFlag)
                .build();

    }

    @Override
    @Transactional
    public HistoryResponseDto.DeleteHistoryResultDto deleteHistory(HttpServletRequest httpServletRequest, Long postId) throws IOException {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        History history = historyRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.HISTORY_NOT_FOUND));
        HistoryResponseDto.DeleteHistoryResultDto deleteHistoryResultDto = HistoryConverter.toDeleteHistoryResultDto(history);

        if (loginMemberId.equals(history.getPost().getMemberId())) {
            s3Manager.deleteFile(history.getThumbnailImageUrl());
            historyRepository.delete(history);
        } else {
            //  사용자와 작성자가 다르면 예외처리
            throw new ExceptionHandler(ErrorStatus._FORBIDDEN);
        }

        return deleteHistoryResultDto;
    }

    @Override
    @Transactional
    public History updateHistory(HttpServletRequest httpServletRequest, Long postId, MultipartFile thumbnailImage, HistoryRequestDto.UpdateHistoryDto updateHistoryDto) throws IOException {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        History history = historyRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.HISTORY_NOT_FOUND));
        if (loginMemberId.equals(history.getPost().getMemberId())) {
            // s3에 업로드 되어있는 기존 데이터들을 제거
            s3Manager.deleteFile(history.getThumbnailImageUrl());

            // 수정한 내용을 s3에 업로드
            String updatedThumbnailImageUrl = s3Manager.uploadFileToDirectory(amazonConfig.getHistoryThumbnailPath(), uuidCreator.createUuid(), thumbnailImage);

            if (updateHistoryDto.getAddress() != null) {
                history.getPost().updateAddress(updateHistoryDto.getAddress());
            }
            if (updateHistoryDto.getLongitude() != null) {
                history.getPost().updateLongitude(updateHistoryDto.getLongitude());
            }
            if (updateHistoryDto.getLatitude() != null) {
                history.getPost().updateLatitude(updateHistoryDto.getLatitude());
            }
            if (updateHistoryDto.getTitle() != null) {
                history.updateTitle(updateHistoryDto.getTitle());
            }
            if (updateHistoryDto.getBodyPreview() != null) {
                history.updateBodyPreview(updateHistoryDto.getBodyPreview());
            }
            history.updateThumbnailImageUrl(updatedThumbnailImageUrl);
        } else {
            throw new ExceptionHandler(ErrorStatus._FORBIDDEN);
        }

        return history;
    }

    @Override
    public String historyImageUrl(MultipartFile historyImage) {

        String historyImageUrl = s3Manager.uploadFileToDirectory(amazonConfig.getHistoryImagePath(), uuidCreator.createUuid(), historyImage);

        return historyImageUrl;
    }


}
