package com.dto.way.post.service.CommentService;

import com.dto.way.post.converter.CommentConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Post;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.PostRepository;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment createComment(Authentication auth, Long postId, CommentRequestDto.CreateCommentDto createCommentDto) {

        String email = auth.getName();
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Comment comment = CommentConverter.toComment(email, post, createCommentDto);

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto.DeleteCommentResultDto deleteComment(Authentication auth, Long commentId) {

        String email = auth.getName();
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        CommentResponseDto.DeleteCommentResultDto deleteCommentResultDto = CommentConverter.toDeleteCommentResultDto(comment);
        if (email.equals(comment.getMemberEmail())) {
            commentRepository.delete(comment);
        } else {
            throw new SecurityException("댓글 작성자만 댓글을 삭제할 수 있습니다. ");
        }


        return deleteCommentResultDto;
    }
}
