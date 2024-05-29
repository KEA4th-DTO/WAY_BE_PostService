package com.dto.way.post.service.historyService;

import com.dto.way.post.converter.HistoryConverter;
import com.dto.way.post.domain.History;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.HistoryRepository;
import com.dto.way.post.repository.LikeRepository;
import com.dto.way.post.service.commentService.CommentCommandService;
import com.dto.way.post.service.likeService.LikeCommandService;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import com.dto.way.post.web.dto.memberDto.MemberResponseDto;
import com.dto.way.post.web.feign.MemberClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HistoryQueryServiceImpl implements HistoryQueryService{

    private final CommentCommandService commentCommandService;
    private final LikeCommandService likeCommandService;
    private final HistoryRepository historyRepository;
    private final LikeRepository likeRepository;
    private final EntityManager entityManager;
    private final JwtUtils jwtUtils;
    private final MemberClient memberClient;

    
    @Override
    @Transactional(readOnly = true)
    public HistoryResponseDto.GetHistoryResultDto getHistoryResult(HttpServletRequest httpServletRequest, Long postId) {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        History history = historyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 히스토리가 존재하지 않습니다."));
        boolean isLiked = likeRepository.existsByPostIdAndMemberId(postId, loginMemberId);
        boolean isOwned = loginMemberId.equals(history.getPost().getMemberId());
        Long countLike = commentCommandService.countComment(postId);
        Long countComment = likeCommandService.countLikes(postId);



        return HistoryResponseDto.GetHistoryResultDto.builder()
                .postId(history.getPostId())
                .title(history.getTitle())
                .bodyPreview(history.getBodyPreview())
                .body(history.getBody())
                .isOwned(isOwned)
                .isLiked(isLiked)
                .likesCount(countLike)
                .commentsCount(countComment)
                .createdAt(history.getCreatedAt()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public HistoryResponseDto.GetHistoryListResultDto getHistoryListByRange(HttpServletRequest httpServletRequest, Double longitude1, Double latitude1, Double longitude2, Double latitude2) {
        String sql = "SELECT p.member_id, p.post_id, h.title, h.body, h.created_at,h.body_preview FROM post p JOIN history h ON h.post_id = p.post_id WHERE ST_Contains(ST_MakeEnvelope(:x1, :y1, :x2, :y2, 4326), p.point) = true";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("x1", longitude1);
        query.setParameter("y1", latitude1);
        query.setParameter("x2", longitude2);
        query.setParameter("y2", latitude2);

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);

        List<Object[]> rawResultList = query.getResultList();
        List<HistoryResponseDto.GetHistoryResultDto> dtoList = rawResultList.stream()
                .map(result -> {
                    HistoryResponseDto.GetHistoryResultDto dto = new HistoryResponseDto.GetHistoryResultDto();
                    Boolean isOwned = result[0].equals(loginMemberId);
                    dto.setIsOwned(isOwned);
                    dto.setPostId((Long) result[1]);
                    dto.setTitle((String) result[2]);
                    dto.setBody((String) result[3]);
                    dto.setCreatedAt(((Timestamp) result[4]).toLocalDateTime());
                    dto.setBodyPreview((String) result[5]);

                    boolean isLiked = likeRepository.existsByPostIdAndMemberId((Long) result[1], (Long) result[0]);
                    dto.setIsLiked(isLiked);

                    MemberResponseDto.GetMemberResultDto writerMemberInfo = memberClient.findMemberByMemberId((Long) result[0]);
                    dto.setWriterNickname(writerMemberInfo.getNickname());
                    dto.setWriterProfileImageUrl(writerMemberInfo.getProfileImageUrl());

                    return dto;
                })
                .collect(Collectors.toList());

        HistoryResponseDto.GetHistoryListResultDto result = new HistoryResponseDto.GetHistoryListResultDto(dtoList);
        return result;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<History> findHistoryByTitle(Integer page, String title) {
        Page<History> historyPage = historyRepository.findByTitleContaining(PageRequest.of(page, 10), title);
        if(historyPage.isEmpty()){
            throw new ResourceNotFoundException("해당 키워드로 검색 결과를 찾지 못했습니다.");
        }
        return historyPage;
    }
    @Override
    @Transactional(readOnly = true)
    public Page<History> findHistoryByBody(Integer page, String body) {

        Page<History> historyPage = historyRepository.findByBodyContaining(PageRequest.of(page, 10), body);
        if (historyPage.isEmpty()) {
            throw new ResourceNotFoundException("해당 키워드로 검색 결과를 찾지 못했습니다.");
        }

        return historyPage;
    }

}
