package com.dto.way.post.web.dto.reportDto;

import com.dto.way.post.domain.enums.ReportType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportRequestDto {

    @Getter
    public static class CreateReportDto {

        @NotNull(message = "신고 타입을 입력해주세요.")
        @Enumerated(EnumType.STRING)
        private ReportType type;

        @NotBlank(message = "신고 제목을 입력해주세요.")
        @Size(max = 50, message = "제목의 최대 길이는 50자 입니댜.")
        private String title;

        @NotBlank(message = "신고 내용을 입력해주세요.")
        @Size(max = 200, message = "본문의 최대 갤이는 200자 입니댜.")
        private String description;
    }
}
