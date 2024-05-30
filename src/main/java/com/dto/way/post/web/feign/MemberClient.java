package com.dto.way.post.web.feign;


import com.dto.way.post.global.config.FeignClientConfig;
import com.dto.way.post.web.dto.memberDto.MemberResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service", url = "${config.feign.member-url}",configuration = FeignClientConfig.class)
public interface MemberClient {

    @GetMapping("/member-info/{email}")
    MemberResponseDto.GetMemberResultDto findMemberByEmail(@PathVariable String email);

    @GetMapping("/member-info/nickname/{nickname}")
    MemberResponseDto.GetMemberResultDto findMemberByNickname(@PathVariable String nickname);

    @GetMapping("/member-info/id/{memberId}")
    MemberResponseDto.GetMemberResultDto findMemberByMemberId(@PathVariable Long memberId);
}
