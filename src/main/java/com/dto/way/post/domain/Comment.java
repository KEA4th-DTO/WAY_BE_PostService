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
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private History history;

    private Long memberId;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Reply> replyList = new ArrayList<>();

    public void updateBody(String body) {
        this.body = body;
    }
}
