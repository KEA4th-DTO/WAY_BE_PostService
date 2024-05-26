package com.dto.way.post.service.commentService;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CommentQueryService {

    CommentResponseDto.GetCommentResultDto getCommentResultDto(HttpServletRequest httpServletRequest, Long commentId);

    CommentResponseDto.GetCommentListResultDto getCommentListResultDto(HttpServletRequest httpServletRequest, Long postId);
}
