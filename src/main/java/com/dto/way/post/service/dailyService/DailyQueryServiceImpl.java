package com.dto.way.post.service.dailyService;

import com.dto.way.post.domain.Daily;
import com.dto.way.post.repository.DailyRepository;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class DailyQueryServiceImpl implements DailyQueryService {

    private final DailyRepository dailyRepository;
    private final EntityManager entityManager;


    @Override
    public Daily getDaily(Long postId) {

        Daily daily = dailyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 데일리가 존재하지 않습니다. "));
        return daily;
    }

    @Override
    public DailyResponseDto.GetDailyListResultDto getDailyListByRange(Double longitude1, Double latitude1, Double longitude2, Double latitude2) {

        String sql = "SELECT p.member_id, p.post_id, d.title, d.image_url, d.body, d.created_at, d.expired_at FROM post p JOIN daily d ON d.post_id = p.post_id WHERE ST_Contains(ST_MakeEnvelope(:x1, :y1, :x2, :y2, 4326), p.point) = true";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("x1", longitude1);
        query.setParameter("y1", latitude1);
        query.setParameter("x2", longitude2);
        query.setParameter("y2", latitude2);

        List<Object[]> rawResultList = query.getResultList();
        List<DailyResponseDto.GetDailyResultDto> dtoList = rawResultList.stream()
                .map(result -> {
                    DailyResponseDto.GetDailyResultDto dto = new DailyResponseDto.GetDailyResultDto();
                    dto.setWriterId((Long) result[0]);
                    dto.setPostId((Long) result[1]);
                    dto.setTitle((String) result[2]);
                    dto.setImageUrl((String) result[3]);
                    dto.setBody((String) result[4]);
                    dto.setCreatedAt(((Timestamp) result[5]).toLocalDateTime());
                    dto.setExpiredAt(((Timestamp) result[6]).toLocalDateTime());
                    return dto;
                })
                .collect(Collectors.toList());

        DailyResponseDto.GetDailyListResultDto result = new DailyResponseDto.GetDailyListResultDto(dtoList);
        return result;
    }
}
