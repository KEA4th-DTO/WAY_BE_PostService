package com.dto.way.post.service.postService;

import com.dto.way.post.converter.PostConverter;
import com.dto.way.post.domain.Post;
import com.dto.way.post.domain.enums.PostType;
import com.dto.way.post.repository.PostRepository;
import com.dto.way.post.web.dto.postDto.PostResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;

    private final EntityManager entityManager;


    @Override
    public List<Post> getPostListByDistance(Double longitude, Double latitude, Integer distance) {

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        log.info(String.valueOf(point));

        String stringPoint = String.valueOf(point);
        List<Post> posts = postRepository.findPostByDistance(stringPoint, distance);
        return posts;
    }

    @Override
    public List<Post> getPostListByRange(Double longitude1, Double latitude1, Double longitude2, Double latitude2) {

        List<Post> posts = postRepository.findPostByRange(longitude1, latitude1, longitude2, latitude2);
        return posts;
    }

    @Override
    public PostResponseDto.GetPinListResultDto getPinListByRange(Double longitude1, Double latitude1, Double longitude2, Double latitude2) {

        String sql = "SELECT p.post_id,  p.latitude, p.longitude, p.post_type FROM post p WHERE ST_Contains(ST_MakeEnvelope(:x1, :y1, :x2, :y2, 4326), p.point) = true";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("x1", longitude1);
        query.setParameter("y1", latitude1);
        query.setParameter("x2", longitude2);
        query.setParameter("y2", latitude2);

        List<Object[]> rawResultList = query.getResultList();
        List<PostResponseDto.GetPinResultDto> dtoList = rawResultList.stream()
                .map(result -> {
                    PostResponseDto.GetPinResultDto dto = new PostResponseDto.GetPinResultDto();
                    dto.setPostId((Long) result[0]);
                    dto.setLatitude((Double) result[1]);
                    dto.setLongitude((Double) result[2]);
                    dto.setPostType(Enum.valueOf(PostType.class,(String) result[3]));
                    return dto;
                })
                .collect(Collectors.toList());

        PostResponseDto.GetPinListResultDto result = new PostResponseDto.GetPinListResultDto(dtoList);
        return result;
    }

}
