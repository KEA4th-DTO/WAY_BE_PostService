package com.dto.way.post.converter;

import com.dto.way.post.domain.Post;
import com.dto.way.post.domain.enums.PostType;
import com.dto.way.post.web.dto.postDto.PostResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {

    public static PostResponseDto.GetPinResultDto toGetPinResultDto(Post post) {
        return PostResponseDto.GetPinResultDto.builder()
                .postType(post.getPostType())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .postId(post.getId()).build();
    }

    public static PostResponseDto.GetPostResultDto toGetPostResultDto(String writerNickname, String writerProfileImageUrl, Long loginMemberId, Post post, boolean isLiked) {

        Boolean isOwned = post.getMemberId().equals(loginMemberId);

        if (post.getPostType() == PostType.DAILY) {
            return PostResponseDto.GetPostResultDto.builder()
                    .bodyPreview(post.getDaily().getBody())
                    .writerProfileImageUrl(writerProfileImageUrl)
                    .writerNickname(writerNickname)
                    .postId(post.getId())
                    .title(post.getDaily().getTitle())
                    .imageUrl(post.getDaily().getImageUrl())
                    .postType(post.getPostType())
                    .isLiked(isLiked)
                    .likesCount((long) post.getLikes().size())
                    .commentsCount(null)
                    .inOwned(isOwned)
                    .createdAt(post.getDaily().getCreatedAt())
                    .expiredAt(post.getDaily().getExpiredAt())
                    .postStatus(post.getPostStatus())
                    .build();
        } else {
            return PostResponseDto.GetPostResultDto.builder()
                    .postId(post.getId())
                    .bodyPreview(post.getHistory().getBodyPreview())
                    .writerProfileImageUrl(writerProfileImageUrl)
                    .writerNickname(writerNickname)
                    .title(post.getHistory().getTitle())
                    .imageUrl(post.getHistory().getThumbnailImageUrl())
                    .postType(post.getPostType())
                    .isLiked(isLiked)
                    .likesCount((long) post.getLikes().size())
                    .commentsCount((long) post.getHistory().getComments().size())
                    .inOwned(isOwned)
                    .createdAt(post.getHistory().getCreatedAt())
                    .postStatus(post.getPostStatus())
                    .build();
        }

    }

    public static PostResponseDto.GetPostListResultDto toGetPostListResultDto(List<PostResponseDto.GetPostResultDto> dtoList) {

//        //   이 로직을 service 로 빼서 isLiked 추가, return 부분만 남겨둔다.
//        List<PostResponseDto.GetPostResultDto> postResultDtoList = posts.stream()
//                .map(post -> PostConverter.toGetPostResultDto(memberEmail, post)).collect(Collectors.toList());
//        //
        return PostResponseDto.GetPostListResultDto.builder()
                .postResultDtoList(dtoList).build();
    }


}
