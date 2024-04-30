package com.dto.way.post.service.likeService;

import com.dto.way.post.domain.Like;
import com.dto.way.post.web.dto.likeDto.LikeResponseDto;
import org.springframework.security.core.Authentication;

public interface LikeCommandService {

    Boolean likePost(Authentication auth, Long postId);

    Long countLikes(Long postId);
}
