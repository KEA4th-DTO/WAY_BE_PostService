package com.dto.way.post.service.postService;

public interface PostCommandService {

    String findWriterEmailByPostId(Long postId);

    String findPostTitleByPostId(Long postId);
}
