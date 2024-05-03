package com.dto.way.post.service.commentService;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.History;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentQueryServiceImpl implements CommentQueryService {
    private final CommentRepository commentRepository;
    private final HistoryRepository historyRepository;
    @Override
    public List<Comment> getCommentList(Long postId) {

        History history = historyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        List<Comment> commentList = commentRepository.findAllByHistory(history);

        return commentList;
    }
}
