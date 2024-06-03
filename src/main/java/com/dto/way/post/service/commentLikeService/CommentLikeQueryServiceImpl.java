package com.dto.way.post.service.commentLikeService;

import com.dto.way.post.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeQueryServiceImpl implements CommentLikeQueryService{

    private final CommentLikeRepository commentLikeRepository;

    @Override
    public Long countCommentLike(Long commentId) {
        return commentLikeRepository.countByCommentId(commentId);
    }

}
