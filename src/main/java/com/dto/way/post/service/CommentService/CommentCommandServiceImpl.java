package com.dto.way.post.service.CommentService;

import com.dto.way.post.converter.CommentConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.History;
import com.dto.way.post.domain.Post;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.HistoryRepository;
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

    private final HistoryRepository historyRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment createComment(Authentication auth, Long postId, CommentRequestDto.CreateCommentDto createCommentDto) {

        String email = auth.getName();
        History history = historyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Comment comment = CommentConverter.toComment(email, history, createCommentDto);

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

    @Override
    @Transactional
    public Comment updateComment(Authentication auth, Long commentId, CommentRequestDto.UpdateCommentDto updateCommentDto) {

        String email = auth.getName();
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (email.equals(comment.getMemberEmail())) {
            if (updateCommentDto.getBody() != null) {
                comment.updateBody(updateCommentDto.getBody());
            } else {
                throw new IllegalArgumentException("수정할 내용을 입력해주세요.");
            }
        } else {
            throw new SecurityException("작성자만 댓글을 수정할 수 있습니다.");

        }

        return commentRepository.save(comment);
    }
}
