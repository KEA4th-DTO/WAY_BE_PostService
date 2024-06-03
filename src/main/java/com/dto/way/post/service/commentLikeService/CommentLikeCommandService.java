package com.dto.way.post.service.commentLikeService;

import jakarta.servlet.http.HttpServletRequest;

public interface CommentLikeCommandService {

    Boolean likeComment(HttpServletRequest httpServletRequest, Long commentId);

}
