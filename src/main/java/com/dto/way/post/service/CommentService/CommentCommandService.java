package com.dto.way.post.service.CommentService;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import org.springframework.security.core.Authentication;

public interface CommentCommandService {
    Comment createComment(Authentication auth, Long postId, CommentRequestDto.CreateCommentDto createCommentDto);
}
