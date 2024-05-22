package com.dto.way.post.domain;

import com.dto.way.post.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History extends BaseEntity {

    @Id
    private Long postId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    private String title;
    private String bodyHtmlUrl;
    private String thumbnailImageUrl;
    private String bodyPreview;

}
