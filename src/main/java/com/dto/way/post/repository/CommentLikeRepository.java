package com.dto.way.post.repository;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByMemberIdAndCommentId(Long memberId, Long commentId);

    Long countByCommentId(Long commentId);

    boolean existsByCommentIdAndMemberId(Long commentId, Long memberId);

}
