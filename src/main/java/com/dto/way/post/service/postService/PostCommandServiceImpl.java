package com.dto.way.post.service.postService;


import com.dto.way.post.domain.Post;
import com.dto.way.post.domain.enums.PostType;
import com.dto.way.post.global.exception.ExceptionHandler;
import com.dto.way.post.global.response.code.status.ErrorStatus;
import com.dto.way.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;

    @Override
    public Long findWriterIdByPostId(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.POST_NOT_FOUND));
        Long writerMemberId = post.getMemberId();
        return writerMemberId;
    }

    @Override
    public String findPostTitleByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.POST_NOT_FOUND));

        String title;

        PostType postType = post.getPostType();
        if (postType == PostType.HISTORY) {
            title = post.getHistory().getTitle();
        } else {
            title = post.getDaily().getTitle();
        }
        return title;
    }
}
