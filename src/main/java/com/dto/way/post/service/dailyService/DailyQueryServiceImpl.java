package com.dto.way.post.service.dailyService;

import com.dto.way.post.domain.Daily;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.DailyRepository;
import com.dto.way.post.repository.LikeRepository;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;
import com.dto.way.post.web.dto.memberDto.MemberResponseDto;
import com.dto.way.post.web.feign.MemberClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class DailyQueryServiceImpl implements DailyQueryService {

    private final DailyRepository dailyRepository;
    private final LikeRepository likeRepository;
    private final EntityManager entityManager;
    private final JwtUtils jwtUtils;
    private final MemberClient memberClient;


    @Override
    public DailyResponseDto.GetDailyResultDto getDailyResultDto(HttpServletRequest httpServletRequest, Long postId) {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        Daily daily = dailyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 데일리가 존재하지 않습니다. "));

        boolean isLiked = likeRepository.existsByPostIdAndMemberId(postId, loginMemberId);
        boolean isOwned = loginMemberId.equals(daily.getPost().getMemberId());

        return DailyResponseDto.GetDailyResultDto.builder()
                .postId(daily.getPostId())
                .title(daily.getTitle())
                .body(daily.getBody())
                .isLiked(isLiked)
                .isOwned(isOwned)
                .likesCount((long) daily.getPost().getLikes().size())
                .imageUrl(daily.getImageUrl())
                .expiredAt(daily.getExpiredAt())
                .createdAt(daily.getCreatedAt())
                .build();
    }

    @Override
    public DailyResponseDto.GetDailyListResultDto getDailyListByRange(HttpServletRequest httpServletRequest, Double longitude1, Double latitude1, Double longitude2, Double latitude2) {


        String sql = "SELECT p.member_id, p.post_id, d.title, d.image_url, d.body, d.created_at, d.expired_at FROM post p JOIN daily d ON d.post_id = p.post_id WHERE ST_Contains(ST_MakeEnvelope(:x1, :y1, :x2, :y2, 4326), p.point) = true";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("x1", longitude1);
        query.setParameter("y1", latitude1);
        query.setParameter("x2", longitude2);
        query.setParameter("y2", latitude2);

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);

        List<Object[]> rawResultList = query.getResultList();
        List<DailyResponseDto.GetDailyResultDto> dtoList = rawResultList.stream()
                .map(result -> {
                    DailyResponseDto.GetDailyResultDto dto = new DailyResponseDto.GetDailyResultDto();
                    Boolean isOwned = result[0].equals(loginMemberId);
                    dto.setIsOwned(isOwned);
                    dto.setPostId((Long) result[1]);
                    dto.setTitle((String) result[2]);
                    dto.setImageUrl((String) result[3]);
                    dto.setBody((String) result[4]);
                    dto.setCreatedAt(((Timestamp) result[5]).toLocalDateTime());
                    dto.setExpiredAt(((Timestamp) result[6]).toLocalDateTime());

                    boolean isLiked = likeRepository.existsByPostIdAndMemberId((Long) result[1], (Long) result[0]);
                    dto.setIsLiked(isLiked);

                    MemberResponseDto.GetMemberResultDto writerMemberInfo = memberClient.findMemberByMemberId((Long) result[0]);
                    dto.setWriterNickname(writerMemberInfo.getNickname());
                    dto.setWriterProfileImageUrl(writerMemberInfo.getProfileImageUrl());

                    return dto;
                })
                .collect(Collectors.toList());

        DailyResponseDto.GetDailyListResultDto result = new DailyResponseDto.GetDailyListResultDto(dtoList);
        return result;
    }
}
