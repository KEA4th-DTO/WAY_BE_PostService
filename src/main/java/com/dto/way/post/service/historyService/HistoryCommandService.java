package com.dto.way.post.service.historyService;

import com.dto.way.post.domain.History;
import com.dto.way.post.web.dto.historyDto.HistoryRequestDto;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import org.locationtech.jts.io.ParseException;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface HistoryCommandService {

    History createHistory(Authentication auth, MultipartFile thumbnailImage, MultipartFile bodyHtml, HistoryRequestDto.CreateHistoryDto createHistoryDto) throws ParseException;

    HistoryResponseDto.DeleteHistoryResultDto deleteHistory(Authentication auth, Long postId) throws IOException;

    String historyImageUrl(MultipartFile historyImage);

}
