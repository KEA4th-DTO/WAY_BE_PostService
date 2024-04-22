package com.dto.way.post.service.postService;

import com.dto.way.post.domain.Location;
import com.dto.way.post.domain.Post;
import com.dto.way.post.domain.enums.Direction;
import com.dto.way.post.repository.PostRepository;
import com.dto.way.post.utils.GeometryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;

    @Override
    public List<Post> getPostListByDistance(Double longitude, Double latitude, Integer distance) {

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        log.info(String.valueOf(point));

        String stringPoint = String.valueOf(point);
        List<Post> posts = postRepository.findByDistance(stringPoint, distance);
        return posts;
    }

    @Override
    public List<Post> getPostListByRange(Double longitude1, Double latitude1, Double longitude2, Double latitude2) {
        List<Post> posts = postRepository.findByRange(longitude1, latitude1, longitude2, latitude2);
        return posts;
    }
}
