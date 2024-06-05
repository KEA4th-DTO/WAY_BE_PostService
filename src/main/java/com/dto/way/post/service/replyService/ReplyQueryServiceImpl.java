package com.dto.way.post.service.replyService;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Reply;
import com.dto.way.post.global.exception.handler.ExceptionHandler;
import com.dto.way.post.global.response.code.status.ErrorStatus;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.ReplyRepository;
import com.dto.way.post.web.dto.memberDto.MemberResponseDto;
import com.dto.way.post.web.dto.replyDto.ReplyResponseDto;
import com.dto.way.post.web.feign.MemberClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyQueryServiceImpl implements ReplyQueryService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;
    private final JwtUtils jwtUtils;
    private final MemberClient memberClient;


    @Override
    public ReplyResponseDto.GetReplyResultDto getReplyResultDto(HttpServletRequest httpServletRequest, Long replyId) {
        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.REPLY_NOT_FOUND));

        Boolean isOwned = reply.getMemberId().equals(loginMemberId);

        return ReplyResponseDto.GetReplyResultDto.builder()
                .body(reply.getBody())
                .isOwned(isOwned)
                .createdAt(reply.getCreatedAt())
                .build();
    }

    @Override
    public ReplyResponseDto.GetReplyListResultDto getReplyListResultDto(HttpServletRequest httpServletRequest,Long commentId) {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->  new ExceptionHandler(ErrorStatus.COMMENT_NOT_FOUND));

        List<Reply> replyList = replyRepository.findAllByComment(comment);

        List<ReplyResponseDto.GetReplyResultDto> replyResultDtoList = replyList.stream()
                .map(reply -> {
                    ReplyResponseDto.GetReplyResultDto dto = new ReplyResponseDto.GetReplyResultDto();
                    dto.setReplyId(reply.getId());
                    dto.setBody(reply.getBody());
                    Boolean isOwned = reply.getMemberId().equals(loginMemberId);
                    dto.setIsOwned(isOwned);
                    dto.setCreatedAt(reply.getCreatedAt());
                    MemberResponseDto.GetMemberResultDto writerMemberInfo = memberClient.findMemberByMemberId(comment.getMemberId());
                    dto.setWriterNickname(writerMemberInfo.getNickname());
                    dto.setWriterProfileImageUrl(writerMemberInfo.getProfileImageUrl());

                    return dto;
                })
                .collect(Collectors.toList());
        return ReplyResponseDto.GetReplyListResultDto.builder()
                .replyResultDtoList(replyResultDtoList)
                .build();


    }


}
