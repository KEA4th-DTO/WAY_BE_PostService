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
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private Post post;

    private String title;
    private String body;
    private String thumbnailImageUrl;
    private String bodyPreview;

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateBody(String body) {
        this.body = body;
    }

    public void updateThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public void updateBodyPreview(String bodyPreview) {
        this.bodyPreview = bodyPreview;
    }
}