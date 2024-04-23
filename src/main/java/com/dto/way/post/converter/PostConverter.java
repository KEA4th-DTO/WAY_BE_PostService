package com.dto.way.post.converter;

import com.dto.way.post.domain.Post;
import com.dto.way.post.web.dto.postDto.PostResponseDto;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostConverter {

    public static PostResponseDto.GetPostResultDto toGetPostResultDto(Post post) {
        return PostResponseDto.GetPostResultDto.builder()
                .id(post.getId())
                .memberId(post.getMemberId())
                .title(post.getDaily().getTitle())
                .body(post.getDaily().getBody())
                .imageUrl(post.getDaily().getImageUrl())
                .longitude(post.getLongitude())
                .latitude(post.getLatitude())
                .postType(post.getPostType())
                .expiredAt(post.getDaily().getExpiredAt()).build();
    }

    public static PostResponseDto.GetPostListResultDto toGetPostListResultDto(List<Post> posts) {
        List<PostResponseDto.GetPostResultDto> postResultDtoList = posts.stream()
                .map(PostConverter::toGetPostResultDto).collect(Collectors.toList());
        return PostResponseDto.GetPostListResultDto.builder()
                .postResultDtoList(postResultDtoList).build();
    }


}
