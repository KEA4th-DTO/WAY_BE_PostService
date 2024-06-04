package com.dto.way.post.converter;

import com.dto.way.post.domain.History;
import com.dto.way.post.domain.Post;
import com.dto.way.post.domain.enums.Expiration;
import com.dto.way.post.domain.enums.PostType;
import com.dto.way.post.web.dto.historyDto.HistoryRequestDto;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryConverter {

    public static History toHistory(Point point, String thumbnailImageUrl, HistoryRequestDto.CreateHistoryDto createHistoryDto) {
        Post post = Post.builder()
                .latitude(createHistoryDto.getLatitude())
                .longitude(createHistoryDto.getLongitude())
                .postStatus(Expiration.NO_EXPIRATION)
                .postType(PostType.HISTORY)
                .address(createHistoryDto.getAddress())
                .point(point).build();

        return History.builder()
                .title(createHistoryDto.getTitle())
                .bodyPreview(createHistoryDto.getBodyPreview())
                .body(createHistoryDto.getBody())
                .thumbnailImageUrl(thumbnailImageUrl)
                .post(post)
                .build();
    }

    public static HistoryResponseDto.CreateHistoryResultDto toCreateHistoryResponseDto(History history) {
        return HistoryResponseDto.CreateHistoryResultDto.builder()
                .postId(history.getPostId())
                .title(history.getTitle())
                .createAt(history.getCreatedAt())
                .build();
    }

    public static HistoryResponseDto.UpdateHistoryResultDto toUpdateHistoryResponseDto(History history) {
        return HistoryResponseDto.UpdateHistoryResultDto.builder()
                .postId(history.getPostId())
                .title(history.getTitle())
                .updateAt(history.getUpdatedAt())
                .build();
    }

    public static HistoryResponseDto.DeleteHistoryResultDto toDeleteHistoryResultDto(History history) {
        return HistoryResponseDto.DeleteHistoryResultDto.builder()
                .postId(history.getPostId())
                .title(history.getTitle())
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static HistoryResponseDto.HistorySearchResultDto toHistorySearchResultDto(History history) {
        return HistoryResponseDto.HistorySearchResultDto.builder()
                .postId(history.getPostId())
                .thumbnailImageUrl(history.getThumbnailImageUrl())
                .title(history.getTitle())
                .bodyPreview(history.getBodyPreview())
                .createdAt(history.getCreatedAt())
                .build();
    }

    public static HistoryResponseDto.HistorySearchResultListDto toHistorySearchResultListDto(Page<History> historyPage) {
        List<HistoryResponseDto.HistorySearchResultDto> historySearchResultDtoList = historyPage.stream()
                .map(HistoryConverter::toHistorySearchResultDto).collect(Collectors.toList());

        return HistoryResponseDto.HistorySearchResultListDto.builder()
                .historySearchResultDtoList(historySearchResultDtoList)
                .isFirst(historyPage.isFirst())
                .isLast(historyPage.isLast())
                .totalElements(historyPage.getTotalElements())
                .totalPage(historyPage.getTotalPages())
                .listSize(historyPage.getSize())
                .build();
    }
}