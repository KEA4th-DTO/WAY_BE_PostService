package com.dto.way.post.web.dto.historyDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

public class HistoryRequestDto {

    @Getter
    @Builder
    public static class CreateHistoryDto {

        @Size(max = 50)
        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @NotBlank(message = "body를 입력해주세요.")
        private String body;

        @NotNull(message = "경도 좌표를 입력해주세요.")
        private Double latitude;

        @NotNull(message = "위도 좌표를 입력해주세요.")
        private Double longitude;

        @NotBlank(message = "주소를 입력해주세요.")
        private String address;

        @NotBlank(message = "미리보기를 입력해주세요.")
        private String bodyPreview;

        @NotBlank
        private String bodyPlainText;
    }

    @Getter
    @Builder
    public static class UpdateHistoryDto {

        @Size(max = 50)
        private String title;

        private String body;

        private Double latitude;

        private Double longitude;

        private String address;

        private String bodyPreview;

        private String bodyPlainText;

    }

}
