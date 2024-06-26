package com.dto.way.post.repository;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long commentId);

    List<Comment> findAllByPost(Post post);

    Long countByPost(Post post);
    Long countByPostId(Long postId);

}
