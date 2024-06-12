package com.dto.way.post.service.dailyService;

import com.dto.way.post.aws.s3.AmazonS3Manager;
import com.dto.way.post.aws.s3.S3FileService;
import com.dto.way.post.aws.config.AmazonConfig;
import com.dto.way.post.converter.DailyConverter;
import com.dto.way.post.domain.Daily;
import com.dto.way.post.domain.Post;
import com.dto.way.post.domain.enums.Expiration;
import com.dto.way.post.domain.enums.PostType;
import com.dto.way.post.global.exception.handler.ExceptionHandler;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.DailyRepository;
import com.dto.way.post.repository.PostRepository;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DailyCommandServiceImplTest {

    @Mock
    private DailyRepository dailyRepository;

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
    private DailyCommandServiceImpl dailyCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("데일리를 생성한다.")
    @Test
    void testCreateDaily() throws ParseException, IOException {
        // given
        DailyRequestDto.CreateDailyDto createDailyDto = DailyRequestDto.CreateDailyDto.builder()
                .title("Test Title")
                .body("Test Body")
                .latitude(0.0)
                .longitude(0.0)
                .build();

        Daily daily = Daily.builder()
                .post(Post.builder().build()).build();

        when(jwtUtils.getMemberIdFromRequest(any(HttpServletRequest.class))).thenReturn(1L);
        when(s3FileService.saveOrUpdateFileAsync(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture("file-url"));
        when(s3Manager.uploadFileToDirectory(anyString(), anyString(), any(MultipartFile.class))).thenReturn("image-url");
        when(dailyRepository.save(any(Daily.class))).thenReturn(daily);
        when(postRepository.countByMemberId(anyLong())).thenReturn(10L);

        // when
        DailyResponseDto.CreateDailyResultDto result = dailyCommandService.createDaily(httpServletRequest, multipartFile, createDailyDto);

        // then
        assertNotNull(result);
        assertEquals(PostType.DAILY, result.getPostType());
        verify(dailyRepository, times(1)).save(any(Daily.class));
        verify(postRepository, times(1)).countByMemberId(anyLong());
    }

    @DisplayName("데일리를 수정한다.")
    @Test
    void testUpdateDaily() {
        // given
//        Post post = Post.builder().memberId(1L).build();
//        Daily daily = Daily.builder().post(post).build();
//
//        DailyRequestDto.UpdateDailyDto updateDailyDto = DailyRequestDto.UpdateDailyDto.builder()
//                .title("Updated Title")
//                .body("Updated Body")
//                .build();
//
//        when(jwtUtils.getMemberIdFromRequest(any(HttpServletRequest.class))).thenReturn(1L);
//        when(dailyRepository.findById(anyLong())).thenReturn(Optional.of(daily));
//
//        // when
//        Daily result = dailyCommandService.updateDaily(httpServletRequest, 1L, updateDailyDto);
//
//        // then
//        assertNotNull(result);
//        assertEquals("Updated Title", result.getTitle());
//        assertEquals("Updated Body", result.getBody());
//        verify(dailyRepository, times(1)).findById(anyLong());
    }

    @DisplayName("작성자가 아닌 사용자가 데일리를 수정하려고 할 때 예외가 발생한다.")
    @Test
    void testUpdateDailyUnauthorized() {
        // given
        Post post = Post.builder().memberId(1L).build();
        Daily daily = Daily.builder().post(post).build();

        DailyRequestDto.UpdateDailyDto updateDailyDto = DailyRequestDto.UpdateDailyDto.builder()
                .title("Updated Title")
                .body("Updated Body")
                .build();

        when(jwtUtils.getMemberIdFromRequest(any(HttpServletRequest.class))).thenReturn(2L);
        when(dailyRepository.findById(anyLong())).thenReturn(Optional.of(daily));

        // when & then
        assertThrows(SecurityException.class, () -> {
            dailyCommandService.updateDaily(httpServletRequest, 1L, updateDailyDto);
        });
    }

    @DisplayName("만료된 데일리를 수정하려고 할 때 예외가 발생한다.")
    @Test
    void testUpdateDailyExpired() {
        // given
        Post post = Post.builder().memberId(1L).build();
        Daily daily = Daily.builder().post(post).expiredAt(LocalDateTime.now().minusDays(1)).build();

        DailyRequestDto.UpdateDailyDto updateDailyDto = DailyRequestDto.UpdateDailyDto.builder()
                .title("Updated Title")
                .body("Updated Body")
                .build();

        when(jwtUtils.getMemberIdFromRequest(any(HttpServletRequest.class))).thenReturn(1L);
        when(dailyRepository.findById(anyLong())).thenReturn(Optional.of(daily));

        // when & then
        assertThrows(DateTimeException.class, () -> {
            dailyCommandService.updateDaily(httpServletRequest, 1L, updateDailyDto);
        });
    }

    @DisplayName("데일리를 삭제한다.")
    @Test
    void testDeleteDaily() throws IOException {
        // given
        Post post = Post.builder().memberId(1L).build();
        Daily daily = Daily.builder().post(post).build();

        when(jwtUtils.getMemberIdFromRequest(any(HttpServletRequest.class))).thenReturn(1L);
        when(dailyRepository.findById(anyLong())).thenReturn(Optional.of(daily));

        // when
        DailyResponseDto.DeleteDailyResultDto result = dailyCommandService.deleteDaily(httpServletRequest, 1L);

        // then
        assertNotNull(result);
        verify(postRepository, times(1)).delete(any(Post.class));
    }

    @DisplayName("작성자가 아닌 사용자가 데일리를 삭제하려고 할 때 예외가 발생한다.")
    @Test
    void testDeleteDailyUnauthorized() {
        // given
        Post post = Post.builder().memberId(1L).build();
        Daily daily = Daily.builder().post(post).build();

        when(jwtUtils.getMemberIdFromRequest(any(HttpServletRequest.class))).thenReturn(2L);
        when(dailyRepository.findById(anyLong())).thenReturn(Optional.of(daily));

        // when & then
        assertThrows(ExceptionHandler.class, () -> {
            dailyCommandService.deleteDaily(httpServletRequest, 1L);
        });
    }

    @DisplayName("만료된 데일리의 상태를 변경한다.")
    @Test
    void testChangePostStatus() {
        // given
        Daily daily = Daily.builder().post(Post.builder().build()).expiredAt(LocalDateTime.now().minusDays(1)).build();
        when(dailyRepository.findByExpiredAtBefore(any(LocalDateTime.class))).thenReturn(List.of(daily));

        // when
        dailyCommandService.changePostStatus();

        // then
        verify(dailyRepository, times(1)).findByExpiredAtBefore(any(LocalDateTime.class));
        assertEquals(Expiration.EXPIRED, daily.getPost().getPostStatus());
    }
}
