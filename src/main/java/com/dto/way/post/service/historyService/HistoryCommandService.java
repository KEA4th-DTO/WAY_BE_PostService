package com.dto.way.post.service.historyService;

import com.dto.way.post.domain.History;
import com.dto.way.post.web.dto.historyDto.HistoryRequestDto;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.locationtech.jts.io.ParseException;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface HistoryCommandService {

    History createHistory(HttpServletRequest httpServletRequest, MultipartFile thumbnailImage, HistoryRequestDto.CreateHistoryDto createHistoryDto) throws ParseException;

    HistoryResponseDto.DeleteHistoryResultDto deleteHistory(HttpServletRequest httpServletRequest, Long postId) throws IOException;

    History updateHistory(HttpServletRequest httpServletRequest, Long postId, MultipartFile bodyHtml, HistoryRequestDto.UpdateHistoryDto updateHistoryDto) throws IOException;

    String historyImageUrl(MultipartFile historyImage);

}
