package com.dto.way.post.service.historyService;

import com.dto.way.post.aws.s3.AmazonS3Manager;
import com.dto.way.post.aws.s3.S3FileService;
import com.dto.way.post.aws.config.AmazonConfig;
import com.dto.way.post.converter.HistoryConverter;
import com.dto.way.post.domain.History;
import com.dto.way.post.domain.Post;
import com.dto.way.post.domain.enums.PostType;
import com.dto.way.post.global.exception.handler.ExceptionHandler;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.HistoryRepository;
import com.dto.way.post.repository.PostRepository;
import com.dto.way.post.web.dto.historyDto.HistoryRequestDto;
import com.dto.way.post.web.dto.historyDto.HistoryResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class HistoryCommandServiceImplTest {

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private AmazonConfig amazonConfig;

    @Mock
    private AmazonS3Manager s3Manager;

    @Mock
    private S3FileService s3FileService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private HistoryCommandServiceImpl historyCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("히스토리를 생성한다.")
    @Test
    void testCreateHistory() throws ParseException, IOException {
        // given
        HistoryRequestDto.CreateHistoryDto createHistoryDto = HistoryRequestDto.CreateHistoryDto.builder()
                .bodyPlainText("plain text")
                .latitude(1.0)
                .longitude(1.0)
                .build();

        History history = History.builder()
                .post(Post.builder().build()).build();

        when(jwtUtils.getMemberIdFromRequest(any(HttpServletRequest.class))).thenReturn(1L);
        when(s3FileService.saveOrUpdateFileAsync(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture("file-url"));
        when(s3Manager.uploadFileToDirectory(anyString(), anyString(), any(MultipartFile.class))).thenReturn("thumbnail-url");
        when(historyRepository.save(any(History.class))).thenReturn(history);
        when(postRepository.countByMemberId(anyLong())).thenReturn(10L);

        // when
        HistoryResponseDto.CreateHistoryResultDto result = historyCommandService.createHistory(httpServletRequest, multipartFile, createHistoryDto);

        // then
        assertNotNull(result);
        assertEquals(PostType.HISTORY, result.getPostType());
        verify(historyRepository, times(1)).save(any(History.class));
        verify(postRepository, times(1)).countByMemberId(anyLong());
    }

    @DisplayName("히스토리를 삭제한다.")
    @Test
    void testDeleteHistory() throws IOException {
        // given
        Post post = Post.builder().memberId(1L).build();
        History history = History.builder().post(post).build();

        when(jwtUtils.getMemberIdFromRequest(any(HttpServletRequest.class))).thenReturn(1L);
        when(historyRepository.findById(anyLong())).thenReturn(Optional.of(history));

        // when
        HistoryResponseDto.DeleteHistoryResultDto result = historyCommandService.deleteHistory(httpServletRequest, 1L);

        // then
        assertNotNull(result);
        verify(postRepository, times(1)).delete(any(Post.class));
    }

    @DisplayName("히스토리를 수정한다.")
    @Test
    void testUpdateHistory() throws IOException {
        // given
        Post post = Post.builder().memberId(1L).build();
        History history = History.builder().post(post).build();

        HistoryRequestDto.UpdateHistoryDto updateHistoryDto = HistoryRequestDto.UpdateHistoryDto.builder()
                .title("Updated Title")
                .build();

        when(jwtUtils.getMemberIdFromRequest(any(HttpServletRequest.class))).thenReturn(1L);
        when(historyRepository.findById(anyLong())).thenReturn(Optional.of(history));

        // when
        History result = historyCommandService.updateHistory(httpServletRequest, 1L, multipartFile, updateHistoryDto);

        // then
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(historyRepository, times(1)).findById(anyLong());
    }

}
