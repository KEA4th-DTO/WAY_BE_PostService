package com.dto.way.post.service.historyService;

import com.dto.way.post.aws.s3.AmazonS3Manager;
import com.dto.way.post.converter.HistoryConverter;
import com.dto.way.post.domain.History;
import com.dto.way.post.domain.Post;
import com.dto.way.post.aws.config.AmazonConfig;
import com.dto.way.post.repository.HistoryRepository;
import com.dto.way.post.utils.UuidCreator;
import com.dto.way.post.web.dto.historyDto.HistoryRequestDto;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class HistoryCommandServiceImpl implements HistoryCommandService {

    private final HistoryRepository historyRepository;
    private final AmazonConfig amazonConfig;
    private final AmazonS3Manager s3Manager;
    private final UuidCreator uuidCreator;


    @Override
    public History createHistory(MultipartFile thumbnailImage, MultipartFile bodyHtml, HistoryRequestDto.CreateHistoryDto createHistoryDto) throws ParseException {

        String thumbnailImageUrl = s3Manager.uploadFileToDirectory(amazonConfig.getHistoryThumbnailPath(), uuidCreator.createUuid(), thumbnailImage);
        String bodyHtmlUrl = s3Manager.uploadFileToDirectory(amazonConfig.getHistoryBodyPath(), uuidCreator.createUuid(), bodyHtml);

        Double latitude = createHistoryDto.getLatitude();
        Double longitude = createHistoryDto.getLongitude();
        Point point = latitude != null && longitude != null ?
                (Point) new WKTReader().read(String.format("POINT(%s %s)", latitude, longitude))
                : null;

        History history = HistoryConverter.toHistory(point, thumbnailImageUrl, bodyHtmlUrl, createHistoryDto);
        Post post = history.getPost();

        post.setMemberId(1L);

        return historyRepository.save(history);
    }

    @Override
    public HistoryResponseDto.DeleteHistoryResultDto deleteHistory(Long postId) throws IOException {

        History history = historyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("히스토리가 존재하지 않습니다."));
        s3Manager.deleteFile(amazonConfig.getHistoryThumbnailPath(), history.getThumbnailImageUrl());
        s3Manager.deleteFile(amazonConfig.getHistoryBodyPath(), history.getBodyHtmlUrl());
        HistoryResponseDto.DeleteHistoryResultDto deleteHistoryResultDto = HistoryConverter.toDeleteHistoryResultDto(history);

        historyRepository.delete(history);

        return deleteHistoryResultDto;
    }
}
