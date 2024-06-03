package com.dto.way.post.service.likeService;

import com.dto.way.post.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeQueryServiceImpl implements LikeQueryService {

    private final LikeRepository likeRepository;
    @Override
    public Long countLikes(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
