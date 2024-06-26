package com.dto.way.post.web.dto.dailyDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class DailyRequestDto {

    @Getter
    @Builder
    public static class CreateDailyDto {

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(max = 50, message = "제목의 최대 길이는 50자 입니다.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 200, message = "데일리 본문의 최대 길이는 200자 입니다.")
        private String body;

        @NotNull(message = "위도 좌표를 입력해주세요.")
        private Double latitude;

        @NotNull(message = "경도 좌표를 입력해주세요.")
        private Double longitude;

        @NotBlank(message = "주소를 입력해주세요.")
        private String address;

        @NotNull(message = "만료 날짜를 입력해주세요.")
        private LocalDateTime expiredAt;
    }

    @Getter
    @Builder
    public static class UpdateDailyDto {


        private String title;

        private String body;
    }
}
