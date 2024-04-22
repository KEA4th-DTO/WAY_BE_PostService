package com.dto.way.post.web.controller;

import com.dto.way.post.converter.PostConverter;
import com.dto.way.post.domain.Post;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.postService.PostQueryService;
import com.dto.way.post.web.dto.postDto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostRestController {
    private final PostQueryService postQueryService;

    @GetMapping("/distance")
    public ApiResponse<PostResponseDto.GetPostListResultDto> getPostsByDistance(@RequestParam Double latitude,
                                                                                @RequestParam Double longitude,
                                                                                @RequestParam Integer distance) {
        List<Post> postList = postQueryService.getPostListByDistance(latitude, longitude, distance);
        return ApiResponse.of(SuccessStatus.POSTS_FOUND_BY_DISTANCE, PostConverter.toGetPostListResultDto(postList));
    }

    @GetMapping("/range")
    public ApiResponse<PostResponseDto.GetPostListResultDto> getPostsByRange(@RequestParam Double latitude1,
                                                                             @RequestParam Double longitude1,
                                                                             @RequestParam Double latitude2,
                                                                             @RequestParam Double longitude2) {

        List<Post> postList = postQueryService.getPostListByRange(latitude1, longitude1, latitude2,longitude2);
        return ApiResponse.of(SuccessStatus.POSTS_FOUND_BY_RANGE, PostConverter.toGetPostListResultDto(postList));
    }
}
