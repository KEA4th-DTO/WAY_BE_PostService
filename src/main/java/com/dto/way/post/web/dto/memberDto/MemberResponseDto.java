package com.dto.way.post.web.dto.memberDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetMemberResultDto {

        private Long memberId;
        private String name;
        private String nickname;
        private String profileImageUrl;
        private String introduce;
        private String memberStatus;
        private String phoneNumber;

    }
}
