package com.dto.way.post.web.controller;

import com.dto.way.message.NotificationMessage;
import com.dto.way.post.converter.PostConverter;
import com.dto.way.post.domain.Post;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.likeService.LikeCommandService;
import com.dto.way.post.service.notificationService.NotificationService;
import com.dto.way.post.service.postService.PostCommandService;
import com.dto.way.post.service.postService.PostQueryService;
import com.dto.way.post.web.dto.likeDto.LikeResponseDto;
import com.dto.way.post.web.dto.postDto.PostResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post-service/posts")
@RequiredArgsConstructor
public class PostRestController {
    private final PostQueryService postQueryService;
    private final LikeCommandService likeCommandService;
    private final PostCommandService postCommandService;
    private final NotificationService notificationService;


//    @Operation(summary = "(사용하지 않음)게시글 목록 거리로 조회 API", description = "RequestParam 으로 현재 위치와 조회할 거리를 전송해주세요.")
//    @GetMapping("/distance")
//    public ApiResponse<PostResponseDto.GetPostListResultDto> getPostsByDistance(Authentication auth,
//                                                                                @RequestParam Double latitude,
//                                                                                @RequestParam Double longitude,
//                                                                                @RequestParam Integer distance) {
//
//        String loginMemberEmail = auth.getName();
//        List<Post> postList = postQueryService.getPostListByDistance(latitude, longitude, distance);
//        return ApiResponse.of(SuccessStatus.POSTS_FOUND_BY_DISTANCE, PostConverter.toGetPostListResultDto(loginMemberEmail, postList));
//    }

    @Operation(summary = "게시글(Daily, History) 목록을 범위로 조회 API", description = "로컬맵 화면에서 게시글 목록을 조회하기 위한 API 입니다. RequestParam 형식으로 지도의 좌하단 좌표, 우상단 좌표를 전송해주세요.")
    @GetMapping("/list/range")
    public ApiResponse<PostResponseDto.GetPostListResultDto> getPostsByRange(HttpServletRequest httpServletRequest,
                                                                             @RequestParam Double latitude1,
                                                                             @RequestParam Double longitude1,
                                                                             @RequestParam Double latitude2,
                                                                             @RequestParam Double longitude2) {

        List<PostResponseDto.GetPostResultDto> getPostResultDtoList = postQueryService.getPostListByRange(httpServletRequest, latitude1, longitude1, latitude2, longitude2);

        return ApiResponse.of(SuccessStatus.POSTS_FOUND_BY_RANGE, PostConverter.toGetPostListResultDto(getPostResultDtoList));
    }

    @Operation(summary = "게시글(Daily, History) 목록을 사용자 정보로 조회 API", description = "마이맵 화면에서 게시글 목록을 조회하기 위한 API 입니다. PathVariable 으로 사용자 정보(닉네임)를 전송해주세요.")
    @GetMapping("/list/{memberNickname}")
    public ApiResponse<PostResponseDto.GetPostListResultDto> getPersonalPostsByRange(HttpServletRequest httpServletRequest,
                                                                                     @PathVariable(name = "memberNickname") String memberNickname) {

        List<PostResponseDto.GetPostResultDto> getPostResultDtoList = postQueryService.getPersonalPostListByRange(httpServletRequest, memberNickname);

        return ApiResponse.of(SuccessStatus.POSTS_FOUND_BY_RANGE_PERSONAL, PostConverter.toGetPostListResultDto(getPostResultDtoList));
    }

    @Operation(summary = "핀 목록을 범위로 조회 API", description = "로컬맵의 지도에 핀을 띄우기 위한 API 입니다. RequestParam 형식으로 지도의 좌하단 좌표, 우상단 좌표를 전송해주세요.")
    @GetMapping("/pin/range")
    public ApiResponse<PostResponseDto.GetPinListResultDto> getPinsByRange(@RequestParam Double latitude1,
                                                                           @RequestParam Double longitude1,
                                                                           @RequestParam Double latitude2,
                                                                           @RequestParam Double longitude2) {

        PostResponseDto.GetPinListResultDto pinList = postQueryService.getPinListByRange(latitude1, longitude1, latitude2, longitude2);
        return ApiResponse.of(SuccessStatus.PINS_FOUND_BY_RANGE, pinList);
    }


    @Operation(summary = "핀 목록을 범위, 사용자 정보를 사용하여 조회 API", description = "마이맵의 지도에 핀을 띄우기 위한 API 입니다. PathVariable 으로 사용자 정보(닉네임)를 전송해주세요.")
    @GetMapping("/pin/{memberNickname}")
    public ApiResponse<PostResponseDto.GetPinListResultDto> getPersonalPinsByRange(@PathVariable(name = "memberNickname") String memberNickname) {

        PostResponseDto.GetPinListResultDto pinList = postQueryService.getPersonalPinListByRange(memberNickname);
        return ApiResponse.of(SuccessStatus.PINS_FOUND_BY_RANGE_PERSONAL, pinList);
    }

    @Operation(summary = "게시글(Daily, History) 좋아요 기능 API", description = "게시글에 좋아요/좋아요 취소 기능을 하는 API 입니다. PathVariable 으로 좋아요 처리를 할 게시글의 postId 를 전송해주세요.")
    @PostMapping("/like/{postId}")
    public ApiResponse<LikeResponseDto.LikeResultDto> likePost(HttpServletRequest httpServletRequest,
                                                               @PathVariable(name = "postId") Long postId) {
        Boolean isLiked = likeCommandService.likePost(httpServletRequest, postId);
        LikeResponseDto.LikeResultDto dto = new LikeResponseDto.LikeResultDto(postId, likeCommandService.countLikes(postId));

        SuccessStatus status;
        String message;

        Long writerMemberId = postCommandService.findWriterIdByPostId(postId);
        String title = postCommandService.findPostTitleByPostId(postId);

        if (isLiked) {
            status = SuccessStatus.POST_LIKE;
//            message = auth.getName() + "님이 \"" + title + "\"에 좋아요를 눌렀습니다. ";
//            NotificationMessage notificationMessage = notificationService.createNotificationMessage(writerEmail, message);

            // Kafka로 메세지 전송
//            notificationService.postNotificationCreate(notificationMessage);
        } else {
            status = SuccessStatus.POST_UNLIKE;
        }

        return ApiResponse.of(status, dto);
    }

    @Operation(summary = "특정 사용자의 게시글 수를 반환하는 openFeign을 위한 API 입니다.", description = "RequestParam으로 memberId를 전송해주세요.")
    @GetMapping("/count/")
    public PostResponseDto.GetPostCountDto getPostCount(@RequestParam Long memberId) {
        return postQueryService.getPostCount(memberId);
    }

}
