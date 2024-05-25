package com.dto.way.post.service.historyService;

import com.dto.way.post.domain.History;
import com.dto.way.post.repository.HistoryRepository;
import com.dto.way.post.repository.LikeRepository;
import com.dto.way.post.service.commentService.CommentCommandService;
import com.dto.way.post.service.likeService.LikeCommandService;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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

    
    @Override
    public HistoryResponseDto.GetHistoryResultDto getHistoryResultDto(Authentication auth, Long postId) {

        String loginMemberEmail = auth.getName();
        History history = historyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 히스토리가 존재하지 않습니다."));
        boolean isLiked = likeRepository.existsByPostIdAndMemberEmail(postId, loginMemberEmail);
        boolean isOwned = loginMemberEmail.equals(history.getPost().getMemberEmail());
        Long countLike = commentCommandService.countComment(postId);
        Long countComment = likeCommandService.countLikes(postId);

        return HistoryResponseDto.GetHistoryResultDto.builder()
                .postId(history.getPostId())
                .memberEmail(history.getPost().getMemberEmail())
                .title(history.getTitle())
                .bodyHtmlUrl(history.getBodyHtmlUrl())
                .bodyPreview(history.getBodyPreview())
                .isOwned(isOwned)
                .isLiked(isLiked)
                .likesCount(countLike)
                .commentsCount(countComment)
                .createdAt(history.getCreatedAt()).build();
    }

    @Override
    public HistoryResponseDto.GetHistoryListResultDto getHistoryListByRange(Authentication auth, Double longitude1, Double latitude1, Double longitude2, Double latitude2) {
        String sql = "SELECT p.member_email, p.post_id, h.title, h.body_html_url, h.created_at,h.body_preview FROM post p JOIN history h ON h.post_id = p.post_id WHERE ST_Contains(ST_MakeEnvelope(:x1, :y1, :x2, :y2, 4326), p.point) = true";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("x1", longitude1);
        query.setParameter("y1", latitude1);
        query.setParameter("x2", longitude2);
        query.setParameter("y2", latitude2);

        String loginMemberEmail = auth.getName();

        List<Object[]> rawResultList = query.getResultList();
        List<HistoryResponseDto.GetHistoryResultDto> dtoList = rawResultList.stream()
                .map(result -> {
                    HistoryResponseDto.GetHistoryResultDto dto = new HistoryResponseDto.GetHistoryResultDto();
                    Boolean isOwned = ((String) result[0]).equals(loginMemberEmail);
                    dto.setIsOwned(isOwned);
                    dto.setMemberEmail((String) result[0]);
                    dto.setPostId((Long) result[1]);
                    dto.setTitle((String) result[2]);
                    dto.setBodyHtmlUrl((String) result[3]);
                    dto.setCreatedAt(((Timestamp) result[4]).toLocalDateTime());
                    dto.setBodyPreview((String) result[5]);
                    boolean isLiked = likeRepository.existsByPostIdAndMemberEmail((Long) result[1], (String) result[0]);
                    dto.setIsLiked(isLiked);
                    return dto;
                })
                .collect(Collectors.toList());

        HistoryResponseDto.GetHistoryListResultDto result = new HistoryResponseDto.GetHistoryListResultDto(dtoList);
        return result;
    }
}
