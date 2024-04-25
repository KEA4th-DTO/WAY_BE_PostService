package com.dto.way.post.repository;

import com.dto.way.post.domain.Post;
import com.dto.way.post.web.dto.postDto.PostResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select * from Post p " +
            "where ST_Dwithin(p.point, ST_GeomFromText(:point, 4326) ,:distance,false) = true ", nativeQuery = true)
    List<Post> findPostByDistance(@Param("point") String point,
                                  @Param("distance") Integer distance);


    @Query(value = "SELECT * FROM Post p " +
            "WHERE ST_Contains(ST_MakeEnvelope(:x1, :y1, :x2, :y2, 4326), p.point) = true", nativeQuery = true)
    List<Post> findPostByRange(@Param("x1") Double left_x,
                               @Param("y1") Double left_y,
                               @Param("x2") Double right_x,
                               @Param("y2") Double right_y);

    @Query(value = "SELECT new com.dto.way.post.web.dto.postDto.PostResponseDto.GetPinResultDto(p.id,  p.latitude, p.longitude,p.postType) FROM Post p " +
            "WHERE ST_Contains(ST_MakeEnvelope(:x1, :y1, :x2, :y2, 4326), p.point) = true",nativeQuery = true)
    List<PostResponseDto.GetPinResultDto> findPinByRange(@Param("x1") Double left_x,
                                                         @Param("y1") Double left_y,
                                                         @Param("x2") Double right_x,
                                                         @Param("y2") Double right_y);
}