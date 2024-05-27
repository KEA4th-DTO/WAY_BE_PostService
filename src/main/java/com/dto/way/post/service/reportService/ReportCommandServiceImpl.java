package com.dto.way.post.service.reportService;

import com.dto.way.post.converter.ReportConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Post;
import com.dto.way.post.domain.Reply;
import com.dto.way.post.domain.Report;
import com.dto.way.post.domain.enums.ReportStatus;
import com.dto.way.post.domain.enums.ReportType;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.PostRepository;
import com.dto.way.post.repository.ReplyRepository;
import com.dto.way.post.repository.ReportRepository;
import com.dto.way.post.web.dto.reportDto.ReportRequestDto;
import com.dto.way.post.web.dto.reportDto.ReportResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportCommandServiceImpl implements ReportCommandService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final ReportRepository reportRepository;
    private final JwtUtils jwtUtils;

    @Override
    public Report createReport(HttpServletRequest httpServletRequest, Long targetId, ReportRequestDto.CreateReportDto request) {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        ReportType reportType = request.getType();
        Report report = new Report();

        if (reportType.equals(ReportType.POST)) {
            Post post = postRepository.findById(targetId).orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
            report = ReportConverter.toReport(loginMemberId, post.getId(), request);

        }

        if (reportType.equals(ReportType.COMMENT)) {
            Comment comment = commentRepository.findByCommentId(targetId).orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));
            report = ReportConverter.toReport(loginMemberId, comment.getCommentId(), request);

        }

        if (reportType.equals(ReportType.REPLY)) {
            Reply reply = replyRepository.findByReplyId(targetId).orElseThrow(() -> new EntityNotFoundException("대댓글이 존재하지 않습니다."));
            report = ReportConverter.toReport(loginMemberId, reply.getReplyId(), request);


        }
        return reportRepository.save(report);
    }

    @Override
    @Transactional
    public ReportResponseDto.ChangeReportStatusResultDto changeReportStatus(Long reportId, ReportStatus reportStatus) {

        Report report = reportRepository.findById(reportId).orElseThrow(() -> new EntityNotFoundException("신고 내역이 존재하지 않습니다."));
        if (!report.getStatus().equals(reportStatus)) {
            report.updateStatus(reportStatus);
        } else {
            throw new IllegalArgumentException("동일한 상태로는 변경이 불가능 합니다.");
        }

        return ReportConverter.toChangeReportStatusResultDto(report);
    }
}
