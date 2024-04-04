package com.dto.way.post.domain;

import com.dto.way.post.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Daily extends BaseEntity {

    @Id
    private Long postId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String title;
    private String body;
    private String imageUrl;

    /**
     * 데일리 만료 시간은 어떻게 넘어 올 것인지. 일단 저장은 날짜-시간 형태의 LocalDateTime이 좋을거 같은데. 여러가지로 LocalDateTime이 좋을 듯
     * 생성시간 + 사용자 지정 시간 or 만료 시간(날짜-시간)
     */
    private LocalDateTime expiredAt;

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateBody(String body) {
        this.body = body;
    }

}
