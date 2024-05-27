package com.dto.way.post.service.likeService;

import com.dto.way.post.domain.Like;
import com.dto.way.post.web.dto.likeDto.LikeResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface LikeCommandService {

    Boolean likePost(HttpServletRequest httpServletRequest, Long postId);

    Long countLikes(Long postId);
}
