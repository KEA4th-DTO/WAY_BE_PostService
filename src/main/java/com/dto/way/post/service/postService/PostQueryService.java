package com.dto.way.post.service.postService;

import com.dto.way.post.domain.Post;
import com.dto.way.post.web.dto.postDto.PostResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PostQueryService {

    List<Post> getPostListByDistance(Double latitude, Double longitude, Integer distance);

    List<PostResponseDto.GetPostResultDto> getPostListByRange(HttpServletRequest httpServletRequest, Double longitude1, Double latitude1, Double longitude2, Double latitude2);

    List<PostResponseDto.GetPostResultDto> getPersonalPostListByRange(HttpServletRequest httpServletRequest, String memberNickname);

    List<PostResponseDto.GetPostResultDto> getMyPostListByRange(HttpServletRequest httpServletRequest);

    PostResponseDto.GetPinListResultDto getPinListByRange(Double longitude1, Double latitude1, Double longitude2, Double latitude2);

    PostResponseDto.GetPinListResultDto getPersonalPinListByRange(String memberNickname);

    PostResponseDto.GetPinListResultDto getMyPinListByRange(HttpServletRequest httpServletRequest);

    PostResponseDto.GetPostCountDto getPostCount(Long memberId);
}