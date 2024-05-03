package com.dto.way.post.converter;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Reply;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import com.dto.way.post.web.dto.replyDto.ReplyRequestDto;
import com.dto.way.post.web.dto.replyDto.ReplyResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReplyConverter {

    public static Reply toReply(String email, Comment comment, ReplyRequestDto.CreateReplyDto request) {
        return Reply.builder()
                .memberEmail(email)
                .body(request.getBody())
                .comment(comment)
                .build();
    }

    public static ReplyResponseDto.CreateReplyResultDto toCreateReplyResultDto(Reply reply) {
        return ReplyResponseDto.CreateReplyResultDto.builder()
                .replyId(reply.getReplyId())
                .createAt(reply.getCreatedAt())
                .build();
    }

    public static ReplyResponseDto.DeleteReplyResultDto toDeleteReplyResultDto(Reply reply) {
        return ReplyResponseDto.DeleteReplyResultDto.builder()
                .replyId(reply.getReplyId())
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static ReplyResponseDto.UpdateReplyResultDto toUpdateReplyResultDto(Reply reply) {
        return ReplyResponseDto.UpdateReplyResultDto.builder()
                .replyId(reply.getReplyId())
                .updatedAt(reply.getUpdatedAt())
                .build();
    }
    public static ReplyResponseDto.GetReplyResultDto toGetReplyResultDto(Reply reply) {
        return ReplyResponseDto.GetReplyResultDto.builder()
                .memberEmail(reply.getMemberEmail())
                .body(reply.getBody())
                .createdAt(reply.getCreatedAt())
                .build();
    }
    public static ReplyResponseDto.GetReplyListResultDto toGetReplyListResultDto(List<Reply> replyList) {

        List<ReplyResponseDto.GetReplyResultDto> replyResultDtoLsit = replyList.stream()
                .map(ReplyConverter::toGetReplyResultDto).collect(Collectors.toList());
        return ReplyResponseDto.GetReplyListResultDto.builder()
                .replyResultDtoList(replyResultDtoLsit)
                .build();
    }

}
