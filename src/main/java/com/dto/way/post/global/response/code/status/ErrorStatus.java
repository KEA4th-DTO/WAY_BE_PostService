package com.dto.way.post.global.response.code.status;

import com.dto.way.post.global.response.code.BaseErrorCode;
import com.dto.way.post.global.response.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _EMPTY_FIELD(HttpStatus.NO_CONTENT, "COMMON404", "입력 값이 누락되었습니다."),

    // 게시글 관련 응답
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4001", "게시글이 존재하지 않습니다."),

    // 히스토리 관련 응답
    HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "HISTORY4001", "히스토리가 존재하지 않습니다."),

    // 데일리 관련 응답
    DAILY_NOT_FOUND(HttpStatus.NOT_FOUND, "DAILY4001", "데일리가 존재하지 않습니다."),

    // 댓글 관련 응답
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT4001", "댓글이 존재하지 않습니다."),

    // 댓글 관련 응답
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "REPLY4001", "대댓글이 존재하지 않습니다."),

    // 신고 관련 응답
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "REPORT4001", "신고내역이 존재하지 않습니다."),

    //  카프카 관련 응답
    KAFKA_CONSTRUCT_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "KAFKA4001", "KAFKA 연결 실패")

    ;



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
