package com.dto.way.post.repository;

import com.dto.way.post.domain.Like;
import com.dto.way.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

//    Optional<Like> findByMemberEmailAndPostId(String memberEmail, Long postId);
    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);


    Long countByPostId(Long postId);

//    boolean existsByPostIdAndMemberEmail(Long postId, String memberEmail);

    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

}
