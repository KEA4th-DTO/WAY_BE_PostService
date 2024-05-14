package com.dto.way.post.web.controller;

import com.dto.way.post.converter.PostConverter;
import com.dto.way.post.domain.Post;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.likeService.LikeCommandService;
import com.dto.way.post.service.postService.PostQueryService;
import com.dto.way.post.web.dto.likeDto.LikeResponseDto;
import com.dto.way.post.web.dto.postDto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/post-service/posts")
@RequiredArgsConstructor
public class PostRestController {
    private final PostQueryService postQueryService;
    private final LikeCommandService likeCommandService;

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

        List<Post> postList = postQueryService.getPostListByRange(latitude1, longitude1, latitude2, longitude2);
        return ApiResponse.of(SuccessStatus.POSTS_FOUND_BY_RANGE, PostConverter.toGetPostListResultDto(postList));
    }

    @GetMapping("/pin/range")
    public ApiResponse<PostResponseDto.GetPinListResultDto> getPinsByRange(@RequestParam Double latitude1,
                                                                           @RequestParam Double longitude1,
                                                                           @RequestParam Double latitude2,
                                                                           @RequestParam Double longitude2) {

        PostResponseDto.GetPinListResultDto pinList = postQueryService.getPinListByRange(latitude1, longitude1, latitude2, longitude2);
        return ApiResponse.of(SuccessStatus.PINS_FOUND_BY_RANGE, pinList);
    }

    @PostMapping("/like/{postId}")
    public ApiResponse<LikeResponseDto.LikeResultDto> likePost(@PathVariable(name = "postId") Long postId,
                                                               Authentication auth) {
        Boolean isLiked = likeCommandService.likePost(auth, postId);
        LikeResponseDto.LikeResultDto dto = new LikeResponseDto.LikeResultDto(postId, likeCommandService.countLikes(postId));
        SuccessStatus status;
        if (isLiked) status = SuccessStatus.POST_LIKE;
        else status = SuccessStatus.POST_UNLIKE;

        return ApiResponse.of(status, dto);
    }
}
