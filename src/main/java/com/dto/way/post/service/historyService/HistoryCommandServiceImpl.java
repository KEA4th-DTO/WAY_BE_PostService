package com.dto.way.post.service.historyService;

import com.dto.way.post.aws.s3.AmazonS3Manager;
import com.dto.way.post.aws.s3.S3FileService;
import com.dto.way.post.converter.HistoryConverter;
import com.dto.way.post.domain.History;
import com.dto.way.post.domain.Post;
import com.dto.way.post.aws.config.AmazonConfig;
import com.dto.way.post.domain.enums.PostType;
import com.dto.way.post.global.exception.handler.ExceptionHandler;
import com.dto.way.post.global.response.code.status.ErrorStatus;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.HistoryRepository;
import com.dto.way.post.repository.PostRepository;
import com.dto.way.post.web.dto.historyDto.HistoryRequestDto;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
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
import java.util.UUID;
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
    private final JwtUtils jwtUtils;


    @Override
    @Transactional
    public HistoryResponseDto.CreateHistoryResultDto createHistory(HttpServletRequest httpServletRequest, MultipartFile thumbnailImage, HistoryRequestDto.CreateHistoryDto createHistoryDto) throws ParseException {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);

        // AI 분석 데이터를 위해 HTML 태그가 제거된 history 본문 내용을 S3 파일에 비동기적으로 저장
        CompletableFuture<String> futureTextFile = s3FileService.saveOrUpdateFileAsync(
                createHistoryDto.getBodyPlainText(),
                amazonConfig.getAiText() + "/" + "text_member_id_" + loginMemberId + ".txt"
        );

        // 썸네일 이미지를 S3에 동기적으로 업로드하고 URL을 반환
        String thumbnailImageUrl = s3Manager.uploadFileToDirectory(
                amazonConfig.getHistoryThumbnailPath(),
                loginMemberId + "_" + UUID.randomUUID().toString(),
                thumbnailImage
        );

        // 좌표 데이터를 처리하여 Point 객체 생성
        Double latitude = createHistoryDto.getLatitude();
        Double longitude = createHistoryDto.getLongitude();
        Point point = (latitude != null && longitude != null) ?
                (Point) new WKTReader().read(String.format("POINT(%s %s)", latitude, longitude)) : null;

        // History 엔티티 생성
        History history = HistoryConverter.toHistory(point, thumbnailImageUrl, createHistoryDto);
        Post post = history.getPost();
        post.setMemberId(loginMemberId);

        // History 엔티티를 데이터베이스에 저장
        History createdHistory = historyRepository.save(history);
        Long count = postRepository.countByMemberId(loginMemberId);

        // 비동기 작업 예외 처리
        futureTextFile.exceptionally(ex -> {
            System.err.println("[S3]history plain text 업로드 실패: " + ex.getMessage());
            return null;
        });

        // 캡처 플래그 설정
        boolean captureFlag = (count > 0) && (count % 10 == 0);

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
            postRepository.delete(history.getPost());
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
        String updatedThumbnailImageUrl;
        if (loginMemberId.equals(history.getPost().getMemberId())) {
            // s3에 업로드 되어있는 기존 데이터들을 제거
            s3Manager.deleteFile(history.getThumbnailImageUrl());

            // 수정한 내용을 s3에 업로드
            if (thumbnailImage != null) {
                updatedThumbnailImageUrl = s3Manager.uploadFileToDirectory(amazonConfig.getHistoryThumbnailPath(),
                        loginMemberId + UUID.randomUUID().toString(),
                        thumbnailImage);
            } else {
                updatedThumbnailImageUrl = history.getThumbnailImageUrl();
            }

            history.updateThumbnailImageUrl(updatedThumbnailImageUrl);

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
            if (updateHistoryDto.getBody() != null) {
                history.updateBody(updateHistoryDto.getBody());
            }
        } else {
            throw new ExceptionHandler(ErrorStatus._FORBIDDEN);
        }

        return history;
    }

    @Override
    public String historyImageUrl(MultipartFile historyImage) {

        String historyImageUrl = s3Manager.uploadFileToDirectory(amazonConfig.getHistoryImagePath(), UUID.randomUUID().toString(), historyImage);

        return historyImageUrl;
    }


}
