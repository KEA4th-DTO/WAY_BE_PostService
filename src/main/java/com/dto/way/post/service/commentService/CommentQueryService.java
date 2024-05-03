package com.dto.way.post.service.commentService;

import com.dto.way.post.domain.Comment;

import java.util.List;

public interface CommentQueryService {

    List<Comment> getCommentList(Long postId);
}
