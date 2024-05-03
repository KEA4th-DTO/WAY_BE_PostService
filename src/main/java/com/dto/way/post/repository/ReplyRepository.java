package com.dto.way.post.repository;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<Reply> findByReplyId(Long replyId);

    Long countByComment(Comment comment);

    List<Reply> findAllByComment(Comment comment);
}
