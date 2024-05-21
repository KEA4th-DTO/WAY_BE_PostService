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
import lombok.RequiredArgsConstructor;
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


    //   사용 안함
    @GetMapping("/distance")
    public ApiResponse<PostResponseDto.GetPostListResultDto> getPostsByDistance(Authentication auth,
                                                                                @RequestParam Double latitude,
                                                                                @RequestParam Double longitude,
                                                                                @RequestParam Integer distance) {

        String loginMemberEmail = auth.getName();
        List<Post> postList = postQueryService.getPostListByDistance(latitude, longitude, distance);
        return ApiResponse.of(SuccessStatus.POSTS_FOUND_BY_DISTANCE, PostConverter.toGetPostListResultDto(loginMemberEmail, postList));
    }

    @GetMapping("/range")
    public ApiResponse<PostResponseDto.GetPostListResultDto> getPostsByRange(Authentication auth,
                                                                             @RequestParam Double latitude1,
                                                                             @RequestParam Double longitude1,
                                                                             @RequestParam Double latitude2,
                                                                             @RequestParam Double longitude2) {
        String loginMemberEmail = auth.getName();
        List<Post> postList = postQueryService.getPostListByRange(latitude1, longitude1, latitude2, longitude2);
        return ApiResponse.of(SuccessStatus.POSTS_FOUND_BY_RANGE, PostConverter.toGetPostListResultDto(loginMemberEmail, postList));
    }

    @GetMapping("/range/{memberEmail}")
    public ApiResponse<PostResponseDto.GetPostListResultDto> getPersonalPostsByRange(Authentication auth,
                                                                                     @PathVariable(name = "memberEmail") String memberEmail) {
        List<Post> postList = postQueryService.getPersonalPostListByRange(memberEmail);
        String loginMemberEmail = auth.getName();

        return ApiResponse.of(SuccessStatus.POSTS_FOUND_BY_RANGE_PERSONAL, PostConverter.toGetPostListResultDto(loginMemberEmail, postList));
    }

    @GetMapping("/pin/range")
    public ApiResponse<PostResponseDto.GetPinListResultDto> getPinsByRange(@RequestParam Double latitude1,
                                                                           @RequestParam Double longitude1,
                                                                           @RequestParam Double latitude2,
                                                                           @RequestParam Double longitude2) {

        PostResponseDto.GetPinListResultDto pinList = postQueryService.getPinListByRange(latitude1, longitude1, latitude2, longitude2);
        return ApiResponse.of(SuccessStatus.PINS_FOUND_BY_RANGE, pinList);
    }

    @GetMapping("/pin/{memberEmail}")
    public ApiResponse<PostResponseDto.GetPinListResultDto> getPersonalPinsByRange(
            @PathVariable(name = "memberEmail") String memberEmail) {

        PostResponseDto.GetPinListResultDto pinList = postQueryService.getPersonalPinListByRange(memberEmail);
        return ApiResponse.of(SuccessStatus.PINS_FOUND_BY_RANGE_PERSONAL, pinList);
    }

    @PostMapping("/like/{postId}")
    public ApiResponse<LikeResponseDto.LikeResultDto> likePost(@PathVariable(name = "postId") Long postId,
                                                               Authentication auth) {
        Boolean isLiked = likeCommandService.likePost(auth, postId);
        LikeResponseDto.LikeResultDto dto = new LikeResponseDto.LikeResultDto(postId, likeCommandService.countLikes(postId));

        SuccessStatus status;
        String message;

        String writerEmail = postCommandService.findWriterEmailByPostId(postId);
        String title = postCommandService.findPostTitleByPostId(postId);

        if (isLiked) {
            status = SuccessStatus.POST_LIKE;
            message = auth.getName() + "님이 \"" + title + "\"에 좋아요를 눌렀습니다. ";
            NotificationMessage notificationMessage = notificationService.createNotificationMessage(writerEmail, message);

            // Kafka로 메세지 전송
            notificationService.postNotificationCreate(notificationMessage);
        } else {
            status = SuccessStatus.POST_UNLIKE;
        }

        return ApiResponse.of(status, dto);
    }
}
