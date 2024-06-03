package com.dto.way.post.service.commentLikeService;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.CommentLike;
import com.dto.way.post.domain.Like;
import com.dto.way.post.global.exception.handler.ExceptionHandler;
import com.dto.way.post.global.response.code.status.ErrorStatus;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.CommentLikeRepository;
import com.dto.way.post.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeCommandServiceImpl implements CommentLikeCommandService{

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public Boolean likeComment(HttpServletRequest httpServletRequest, Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.COMMENT_NOT_FOUND));
        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        Optional<CommentLike> commentLike = commentLikeRepository.findByMemberIdAndCommentId(loginMemberId, comment.getId());

        if (commentLike.isEmpty()) {
            CommentLike newCommentLike = new CommentLike(comment, loginMemberId);
            commentLikeRepository.save(newCommentLike);
            return true;
        } else {
            commentLikeRepository.delete(commentLike.get());
            return false;
        }
    }
}
