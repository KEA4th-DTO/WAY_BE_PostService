package com.dto.way.post.service.postService;

import com.dto.way.post.domain.Post;
import com.dto.way.post.web.dto.postDto.PostResponseDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PostQueryService {

    List<Post> getPostListByDistance(Double latitude, Double longitude, Integer distance);

    List<Post> getPostListByRange( Double longitude1, Double latitude1, Double longitude2, Double latitude2);

    PostResponseDto.GetPinListResultDto getPinListByRange(Double longitude1, Double latitude1, Double longitude2, Double latitude2);

    List<Post> getPersonalPostListByRange( String memberEmail);

    PostResponseDto.GetPinListResultDto getPersonalPinListByRange(String memberEmail);

}