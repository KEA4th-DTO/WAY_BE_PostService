package com.dto.way.post.service.historyService;

import com.dto.way.post.domain.History;
import com.dto.way.post.web.dto.historyDto.HistoryRequestDto;
import org.locationtech.jts.io.ParseException;
import org.springframework.web.multipart.MultipartFile;

public interface HistoryCommandService {

    History createHistory(MultipartFile thumbnailImage, MultipartFile bodyHtml, HistoryRequestDto.CreateHistoryDto createHistoryDto) throws ParseException;


}
