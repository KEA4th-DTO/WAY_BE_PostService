package com.dto.way.post.service.postService;

public interface PostCommandService {

    Long findWriterIdByPostId(Long postId);

    String findPostTitleByPostId(Long postId);
}
