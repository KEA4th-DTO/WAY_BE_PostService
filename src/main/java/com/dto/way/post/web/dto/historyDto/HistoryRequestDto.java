package com.dto.way.post.web.dto.historyDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class HistoryRequestDto {

    @Getter
    public static class CreateHistoryDto {

        @Size(max = 50)
        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @NotNull(message = "경도 좌표를 입력해주세요.")
        private Double latitude;

        @NotNull(message = "위도 좌표를 입력해주세요.")
        private Double longitude;

        @NotBlank(message = "주소를 입력해주세요.")
        private String address;

        @NotBlank(message = "미리보기를 입력해주세요.")
        private String bodyPreview;

    }

}
