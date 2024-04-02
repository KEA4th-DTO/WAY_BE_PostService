package com.dto.way.post.domain;

import com.dto.way.post.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Daily extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String body;
    private String imageUrl;

    //TODO: 프론트에서 좌표로 받을 건지, 주소로 받아서 벡에서 좌표로 변환할건지 논의 필요
    // -> 좌표로 받는게 훨씬 편할 거 같긴한데 뭐가 맞는거지
    private Double latitude;
    private Double longitude;

    /**
     * 데일리 만료 시간은 어떻게 넘어 올 것인지. 일단 저장은 날짜-시간 형태의 LocalDateTime이 좋을거 같은데. 여러가지로 LocalDateTime이 좋을 듯
     * 생성시간 + 사용자 지정 시간 or 만료 시간(날짜-시간)
     */
    private LocalDateTime expiredAt;

    //연관관계 매핑은 추후에 생각
    private Long memberId;

    public void setMemberId(Long memberId) {

        this.memberId = memberId;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateBody(String body) {
        this.body = body;
    }
}
