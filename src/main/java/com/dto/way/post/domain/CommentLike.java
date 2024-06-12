package com.dto.way.post.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @NonNull
    private Comment comment;

    @NonNull
    private Long memberId;

}
