package com.dto.way.post.web.controller;

import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.web.dto.memberDto.MemberResponseDto;
import com.dto.way.post.web.feign.MemberClient;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final MemberClient memberClient;
    private final JwtUtils jwtUtils;

    @GetMapping("/feign")
    public void feignTest() {
        MemberResponseDto.GetMemberResultDto dto = memberClient.findMemberByEmail("naeric7@naver.com");
        log.info("feign name: " + dto.getName());
        log.info("feign nickname: " + dto.getNickname());
        log.info("feign profile image: " + dto.getProfileImageUrl());
    }

    @GetMapping("/jwt")
    public void jwtTest(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getClaim(token);


        log.info("member Email: " + jwtUtils.getEmail(token));

        //  Claims는 key-value로 구성되어 있기 떄문에 memberId의 value를 반환
        log.info("member Id: " + claims.get("memberId"));
        log.info("member Nickname: " + claims.get("nickname"));

    }
}
