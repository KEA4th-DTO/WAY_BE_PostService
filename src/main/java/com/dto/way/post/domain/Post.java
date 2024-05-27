package com.dto.way.post.domain;

import com.dto.way.post.domain.enums.Expiration;
import com.dto.way.post.domain.enums.PostType;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Daily daily;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private History history;

    private Double latitude;
    private Double longitude;

    @Column(columnDefinition = "geometry(Point, 4326)") // PostgreSQL
    private Point point;

    private Long memberId;

    @Enumerated(value = EnumType.STRING)
    private PostType postType;

    @Enumerated(value = EnumType.STRING)
    private Expiration postStatus;

    private String address;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void updateLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void updateLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public void updateExpiration(Expiration postStatus) {
        this.postStatus = postStatus;
    }
}
