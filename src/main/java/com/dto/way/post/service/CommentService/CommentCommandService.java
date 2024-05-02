package com.dto.way.post.service.CommentService;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import org.springframework.security.core.Authentication;

public interface CommentCommandService {
    Comment createComment(Authentication auth, Long postId, CommentRequestDto.CreateCommentDto createCommentDto);

    CommentResponseDto.DeleteCommentResultDto deleteComment(Authentication auth, Long commentId);

    Comment updateComment(Authentication auth, Long commentId, CommentRequestDto.UpdateCommentDto updateCommentDto);

    Long countComment(Long postId);
}
