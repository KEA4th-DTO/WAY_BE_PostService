package com.dto.way.post.converter;

import com.dto.way.post.domain.Post;
import com.dto.way.post.domain.enums.PostType;
import com.dto.way.post.web.dto.postDto.PostResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {

    public static PostResponseDto.GetPostResultDto toGetPostResultDto(Post post) {
        if(post.getPostType()== PostType.DAILY){
            return PostResponseDto.GetPostResultDto.builder()
                    .postId(post.getId())
                    .memberEmail(post.getMemberEmail())
                    .title(post.getDaily().getTitle())
                    .imageUrl(post.getDaily().getImageUrl())
                    .postType(post.getPostType())
                    .likesCount((long) post.getLikes().size())
                    .expiredOrCreatedDate(post.getDaily().getExpiredAt())
                    .build();
        }else {
            return PostResponseDto.GetPostResultDto.builder()
                    .postId(post.getId())
                    .memberEmail(post.getMemberEmail())
                    .title(post.getHistory().getTitle())
                    .imageUrl(post.getHistory().getThumbnailImageUrl())
                    .postType(post.getPostType())
                    .likesCount((long) post.getLikes().size())
                    .expiredOrCreatedDate(post.getHistory().getCreatedAt())
                    .build();
        }

    }

    public static PostResponseDto.GetPostListResultDto toGetPostListResultDto(List<Post> posts) {
        List<PostResponseDto.GetPostResultDto> postResultDtoList = posts.stream()
                .map(PostConverter::toGetPostResultDto).collect(Collectors.toList());
        return PostResponseDto.GetPostListResultDto.builder()
                .postResultDtoList(postResultDtoList).build();
    }


}
