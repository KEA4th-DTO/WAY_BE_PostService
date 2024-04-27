package com.dto.way.post.web.dto.historyDto;

import lombok.Getter;

public class HistoryRequestDto {

    @Getter
    public static class CreateHistoryDto {
        private String title;
        private Double latitude;
        private Double longitude;

    }

}
