package com.dto.way.post.service.likeService;

import com.dto.way.post.domain.Like;
import com.dto.way.post.domain.Post;
import com.dto.way.post.repository.LikeRepository;
import com.dto.way.post.repository.PostRepository;
import com.dto.way.post.web.dto.likeDto.LikeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeCommandServiceImpl implements LikeCommandService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Override
    public Boolean likePost(Authentication auth, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        String memberEmail = auth.getName();
        Optional<Like> like = likeRepository.findByMemberEmailAndPostId(memberEmail, post.getId());

        if(like.isEmpty()){
            Like newLike = new Like(post, memberEmail);
            likeRepository.save(newLike);
            return true;
        }else {
            likeRepository.delete(like.get());
            return false;
        }
    }

    @Override
    public Long countLikes(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
