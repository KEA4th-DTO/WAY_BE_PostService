package com.dto.way.post.service.commentService;

import com.dto.way.post.domain.Comment;

import java.util.List;

public interface CommentQueryService {

    Comment getComment(Long commentId);

    List<Comment> getCommentList(Long postId);
}
