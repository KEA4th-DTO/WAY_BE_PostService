package com.dto.way.post.converter;

import com.dto.way.post.domain.History;
import com.dto.way.post.domain.Post;
import com.dto.way.post.domain.enums.PostType;
import com.dto.way.post.web.dto.historyDto.HistoryRequestDto;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

public class HistoryConverter {

    public static History toHistory(Point point, String thumbnailImageUrl, String bodyHtmlUrl, HistoryRequestDto.CreateHistoryDto createHistoryDto) {
        Post post = Post.builder()
                .latitude(createHistoryDto.getLatitude())
                .longitude(createHistoryDto.getLongitude())
                .postType(PostType.HISTORY)
                .point(point).build();

        return History.builder()
                .title(createHistoryDto.getTitle())
                .bodyHtmlUrl(bodyHtmlUrl)
                .thumbnailImageUrl(thumbnailImageUrl)
                .post(post)
                .build();
    }

    public static HistoryResponseDto.CreateHistoryResponseDto toCreateHistoryResponseDto(History history) {
        return HistoryResponseDto.CreateHistoryResponseDto.builder()
                .postId(history.getPostId())
                .title(history.getTitle())
                .createAt(LocalDateTime.now())
                .build();
    }

}
