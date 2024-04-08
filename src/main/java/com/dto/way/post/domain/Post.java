package com.dto.way.post.domain;

import com.dto.way.post.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Daily daily;

    //TODO: 프론트에서 좌표로 받을 건지, 주소로 받아서 벡에서 좌표로 변환할건지 논의 필요
    // -> 좌표로 받는게 훨씬 편할 거 같긴한데 뭐가 맞는거지
    private Double latitude;
    private Double longitude;

    //연관관계 매핑은 추후에 생각
    //private Member member;
    private Long memberId;

    public void setMemberId(Long memberId) {

        this.memberId = memberId;
    }
}