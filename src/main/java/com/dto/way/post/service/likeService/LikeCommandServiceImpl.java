package com.dto.way.post.service.likeService;

import com.dto.way.post.domain.Like;
import com.dto.way.post.domain.Post;
import com.dto.way.post.global.exception.handler.ExceptionHandler;
import com.dto.way.post.global.response.code.status.ErrorStatus;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.LikeRepository;
import com.dto.way.post.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeCommandServiceImpl implements LikeCommandService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final JwtUtils jwtUtils;

    @Override
    public Boolean likePost(HttpServletRequest httpServletRequest, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.POST_NOT_FOUND));
        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        Optional<Like> like = likeRepository.findByMemberIdAndPostId(loginMemberId, post.getId());

        if (like.isEmpty()) {
            Like newLike = new Like(post, loginMemberId);
            likeRepository.save(newLike);
            return true;
        } else {
            likeRepository.delete(like.get());
            return false;
        }
    }


}
